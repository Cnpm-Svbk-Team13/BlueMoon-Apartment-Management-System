package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.model.Gender;
import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.PersonRepository;
import com.bluemoon.bfms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private final UserRepository users = mock(UserRepository.class);
    private final PersonRepository persons = mock(PersonRepository.class);
    private final AuditLogRepository audits = mock(AuditLogRepository.class);
    private final HouseholdWriteLock writeLock = mock(HouseholdWriteLock.class);
    private final User actor = mock(User.class);
    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(users, persons, audits, writeLock, new BCryptPasswordEncoder(10));
        when(actor.getId()).thenReturn(10L);
        when(actor.isActive()).thenReturn(true);
        when(actor.getRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(users.findById(10L)).thenReturn(Optional.of(actor));
        when(users.getReferenceById(10L)).thenReturn(actor);
        when(persons.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createsNormalizedAccountWithBCryptAndNoHashInSummary() {
        var result = service.createUser(
                " Cashier01 ", "  Nguyen Van A ", "A@example.com", "0912345678",
                UserRole.ROLE_CASHIER, "starter-pass", 10L, "127.0.0.1");

        assertEquals("cashier01", result.username());
        assertEquals("Nguyen Van A", result.fullName());
        assertEquals("a@example.com", result.email());
        assertEquals("0912345678", result.phone());
        assertEquals(UserRole.ROLE_CASHIER, result.role());
        assertTrue(result.active());
        verify(users).save(argThat(user -> user.getPasswordHash().startsWith("$2a$10$")
                && new BCryptPasswordEncoder(10).matches("starter-pass", user.getPasswordHash())));
        verify(audits).save(argThat(audit -> "CREATE_USER".equals(audit.getAction())));
        InOrder lockOrder = inOrder(writeLock, users);
        lockOrder.verify(writeLock).acquire();
        lockOrder.verify(users).findById(10L);
    }

    @Test
    void rejectsDuplicateUsernameBeforeCreatingProfileOrAudit() {
        when(users.existsByUsername("cashier01")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createUser(
                "Cashier01", "Nguyen Van A", "a@example.com", "0912345678",
                UserRole.ROLE_CASHIER, "starter-pass", 10L, null));

        verifyNoInteractions(persons, audits);
        verify(users, never()).save(any(User.class));
    }

    @Test
    void rejectsNonAdministrator() {
        when(actor.getRole()).thenReturn(UserRole.ROLE_ACCOUNTANT);

        assertThrows(IllegalArgumentException.class, () -> service.createUser(
                "cashier01", "Nguyen Van A", "a@example.com", "0912345678",
                UserRole.ROLE_CASHIER, "starter-pass", 10L, null));

        verifyNoInteractions(persons, audits);
    }

    @Test
    void updatesProfileRoleAndActiveStateWithoutChangingOtherPersonFields() {
        User target = targetAccount();
        Person profile = target.getPerson();

        var result = service.updateUser(20L, "new-cashier", "New Name", "new@example.test",
                "0987654321", UserRole.ROLE_CASHIER, false, 10L, null);

        assertEquals("new-cashier", result.username());
        assertEquals("New Name", result.fullName());
        assertEquals("new@example.test", result.email());
        assertFalse(result.active());
        verify(profile).updatePersonalDetails("New Name", "012345678901", "0987654321",
                "new@example.test", LocalDate.of(1990, 1, 1), null);
        verify(target).updateAccount("new-cashier", UserRole.ROLE_CASHIER, false);
        verify(audits).save(argThat(audit -> "UPDATE_USER".equals(audit.getAction())));
        verify(writeLock).acquire();
    }

    @Test
    void statusChangeAndAccountListReturnSafeSummaries() {
        User target = targetAccount();
        when(users.findAllByOrderByIdAsc()).thenReturn(List.of(target));

        var changed = service.toggleUserStatus(20L, false, 10L, null);
        var accounts = service.getAllUsers(10L);

        assertFalse(changed.active());
        assertEquals(1, accounts.size());
        assertEquals("old-user", accounts.getFirst().username());
        assertEquals("Person Name", accounts.getFirst().fullName());
        assertFalse(accounts.getFirst().active());
        verify(users).findAllByOrderByIdAsc();
        verify(writeLock).acquire();
    }

    @Test
    void resetPasswordStoresBcryptAndClearsPreviousLockState() {
        User target = targetAccount();

        service.resetPassword(20L, "replacement-pass", 10L, null);

        var hash = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(target).changePasswordHash(hash.capture());
        assertTrue(new BCryptPasswordEncoder(10).matches("replacement-pass", hash.getValue()));
        verify(target).resetLoginFailureState();
        verify(audits).save(argThat(audit -> "RESET_USER_PASSWORD".equals(audit.getAction())));
        verify(writeLock).acquire();
    }

    private User targetAccount() {
        User target = mock(User.class);
        Person profile = mock(Person.class);
        var username = new java.util.concurrent.atomic.AtomicReference<>("old-user");
        var role = new java.util.concurrent.atomic.AtomicReference<>(UserRole.ROLE_ACCOUNTANT);
        var active = new java.util.concurrent.atomic.AtomicReference<>(true);
        var fullName = new java.util.concurrent.atomic.AtomicReference<>("Person Name");
        var email = new java.util.concurrent.atomic.AtomicReference<>("old@example.test");
        var phone = new java.util.concurrent.atomic.AtomicReference<>("0123456789");

        when(target.getId()).thenReturn(20L);
        when(target.getUsername()).thenAnswer(invocation -> username.get());
        when(target.getRole()).thenAnswer(invocation -> role.get());
        when(target.isActive()).thenAnswer(invocation -> active.get());
        when(target.getPerson()).thenReturn(profile);
        when(profile.getFullName()).thenAnswer(invocation -> fullName.get());
        when(profile.getEmail()).thenAnswer(invocation -> email.get());
        when(profile.getPhone()).thenAnswer(invocation -> phone.get());
        when(profile.getNationalId()).thenReturn("012345678901");
        when(profile.getDateOfBirth()).thenReturn(LocalDate.of(1990, 1, 1));
        when(profile.getGender()).thenReturn(null);
        doAnswer(invocation -> {
            username.set(invocation.getArgument(0));
            role.set(invocation.getArgument(1));
            active.set(invocation.getArgument(2));
            return null;
        }).when(target).updateAccount(anyString(), any(UserRole.class), anyBoolean());
        doAnswer(invocation -> {
            fullName.set(invocation.getArgument(0));
            phone.set(invocation.getArgument(2));
            email.set(invocation.getArgument(3));
            return null;
        }).when(profile).updatePersonalDetails(anyString(), anyString(), anyString(), anyString(),
                any(LocalDate.class), nullable(Gender.class));
        when(users.findById(20L)).thenReturn(Optional.of(target));
        return target;
    }
}
