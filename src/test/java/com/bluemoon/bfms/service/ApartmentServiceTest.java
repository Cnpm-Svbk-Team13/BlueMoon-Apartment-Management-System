package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.ApartmentForm;
import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.AuditLog;
import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.bluemoon.bfms.exception.DuplicateRoomNumberException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Verifies creation and rejection paths for BE-02, US-001 AC 1-2, RAW-001/002.
 */
@ExtendWith(MockitoExtension.class)
class ApartmentServiceTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    private ApartmentService service;
    private ApartmentForm form;

    @BeforeEach
    void setUp() {
        service = new ApartmentService(apartmentRepository, auditLogRepository, userRepository);
        form = new ApartmentForm();
        form.setRoomNumber(" a0601 ");
        form.setFloorNumber(6);
        form.setAreaSqm(new BigDecimal("75.50"));
        form.setApartmentType(ApartmentType.RESIDENTIAL);
    }

    @Test
    void createsNormalizedApartmentAndRecordsActorAndEntityId() {
        Apartment persisted = mock(Apartment.class);
        User actor = mock(User.class);
        when(actor.getId()).thenReturn(7L);
        when(userRepository.getReferenceById(7L)).thenReturn(actor);
        when(persisted.getId()).thenReturn(42L);
        when(persisted.getRoomNumber()).thenReturn("A0601");
        when(apartmentRepository.saveAndFlush(any(Apartment.class))).thenReturn(persisted);

        assertSame(persisted, service.createApartment(form, 7L, "127.0.0.1"));

        ArgumentCaptor<Apartment> apartment = ArgumentCaptor.forClass(Apartment.class);
        verify(apartmentRepository).saveAndFlush(apartment.capture());
        assertEquals("A0601", apartment.getValue().getRoomNumber());
        assertEquals(new BigDecimal("75.50"), apartment.getValue().getAreaSqm());
        verify(apartmentRepository).existsByRoomNumber("A0601");
        ArgumentCaptor<AuditLog> audit = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).saveAndFlush(audit.capture());
        assertEquals(7L, audit.getValue().getUserId());
        assertEquals(42L, audit.getValue().getEntityId());
        assertEquals("CREATE", audit.getValue().getAction());
        assertEquals("Apartment", audit.getValue().getEntityName());
        assertEquals("Created apartment A0601", audit.getValue().getDetails());
        assertEquals("127.0.0.1", audit.getValue().getIpAddress());
    }

    @Test
    void rejectsDuplicateBeforeWriting() {
        when(apartmentRepository.existsByRoomNumber("A0601")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(form, 7L, null));

        verify(apartmentRepository, never()).saveAndFlush(any());
        verifyNoInteractions(auditLogRepository);
    }

    @Test
    void rejectsZeroAreaBeforeDatabaseAccess() {
        form.setAreaSqm(BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(form, 7L, null));

        verifyNoInteractions(apartmentRepository, auditLogRepository);
    }

    @Test
    void rejectsMissingActorBeforeDatabaseAccess() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(form, null, null));

        verifyNoInteractions(apartmentRepository, auditLogRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "0.001", "10000000000.00"})
    void rejectsAreaOutsideStorageContract(String area) {
        form.setAreaSqm(new BigDecimal(area));
        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(form, 7L, null));
        verifyNoInteractions(apartmentRepository, auditLogRepository);
    }

    @Test
    void rejectsMissingFormAndExcessiveIpAddress() {
        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(null, 7L, null));
        assertThrows(IllegalArgumentException.class,
                () -> service.createApartment(form, 7L, "x".repeat(51)));
        verifyNoInteractions(apartmentRepository, auditLogRepository);
    }

    @Test
    void reportsConcurrentUniqueViolationAsDuplicateWithoutWritingAudit() {
        var constraint = new ConstraintViolationException("Duplicate room number",
                new SQLException("Duplicate room number", "23505"), "apartments_room_number_key");
        when(apartmentRepository.saveAndFlush(any(Apartment.class)))
                .thenThrow(new DataIntegrityViolationException("Insert failed", constraint));

        assertThrows(DuplicateRoomNumberException.class,
                () -> service.createApartment(form, 7L, null));
        verifyNoInteractions(auditLogRepository);
    }

    @Test
    void preservesOtherStorageFailuresWithoutWritingAudit() {
        var failure = new DataIntegrityViolationException("Unexpected constraint failure",
                new SQLException("Unexpected constraint failure"));
        when(apartmentRepository.saveAndFlush(any(Apartment.class))).thenThrow(failure);

        assertSame(failure, assertThrows(DataIntegrityViolationException.class,
                () -> service.createApartment(form, 7L, null)));
        verifyNoInteractions(auditLogRepository);
    }
}
