package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.ApartmentForm;
import com.bluemoon.bfms.dto.HouseholdHeadForm;
import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.AuditLog;
import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.HouseholdHeadHistoryRepository;
import com.bluemoon.bfms.repository.HouseholdRepository;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.PersonRepository;
import com.bluemoon.bfms.repository.UserRepository;
import com.bluemoon.bfms.model.HouseholdStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Verifies Spring transaction rollback on H2 for BE-02/BE-03, US-001, RAW-001/002.
 * PostgreSQL constraints and concurrent requests require separate verification.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApartmentCreationTransactionTest {

    @Autowired
    private ApartmentService service;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @MockitoBean
    private AuditLogRepository auditLogRepository;

    @MockitoBean
    private HouseholdWriteLock writeLock;

    @Autowired private HouseholdHeadService headService;
    @Autowired private HouseholdRepository households;
    @Autowired private HouseholdHeadHistoryRepository history;
    @Autowired private PersonRepository persons;
    @Autowired private UserRepository users;
    private Long actorId;

    @BeforeEach
    void seedActor() {
        String username = "actor-" + System.nanoTime();
        Person person = persons.save(new Person(
                "Synthetic Actor", null, "0900000007", username + "@example.test"));
        User actor = users.save(new User(username, "test-hash", person, UserRole.ROLE_ADMIN));
        actorId = actor.getId();
    }

    @Test
    void rollsBackFlushedApartmentWhenAuditWriteFails() {
        ApartmentForm form = new ApartmentForm();
        form.setRoomNumber("ROLLBACK-TEST");
        form.setFloorNumber(6);
        form.setAreaSqm(new BigDecimal("75.50"));
        form.setApartmentType(ApartmentType.RESIDENTIAL);
        when(auditLogRepository.saveAndFlush(any(AuditLog.class)))
                .thenThrow(new IllegalStateException("Simulated audit write failure."));

        assertThrows(IllegalStateException.class,
                () -> service.createApartment(form, actorId, "127.0.0.1"));

        assertFalse(apartmentRepository.existsByRoomNumber("ROLLBACK-TEST"));
    }

    @Test
    void rollsBackHeadHouseholdAndHistoryWhenAuditWriteFails() {
        Long id = apartmentRepository.save(new Apartment("HEAD-ROLLBACK", 6,
                new BigDecimal("75.50"), ApartmentType.RESIDENTIAL)).getId();
        HouseholdHeadForm form = new HouseholdHeadForm();
        form.setMovedInDate(LocalDate.of(2026, 11, 1));
        form.getHeadPerson().setFullName("Rollback Fixture");
        form.getHeadPerson().setNationalId("000000000099");
        form.getHeadPerson().setPhone("0123456789");
        when(auditLogRepository.save(any(AuditLog.class)))
                .thenThrow(new IllegalStateException("Simulated household audit write failure."));

        assertThrows(IllegalStateException.class, () -> headService.assignOrReplace(id, form, actorId, null));

        assertTrue(persons.findByNationalId("000000000099").isEmpty());
        assertTrue(households.findByApartmentIdAndStatus(id, HouseholdStatus.ACTIVE).isEmpty());
        assertTrue(history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(id).isEmpty());
    }
}
