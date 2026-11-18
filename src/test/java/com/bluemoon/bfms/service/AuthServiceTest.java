package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private static final OffsetDateTime NOW = OffsetDateTime.parse("2026-11-14T04:30:00Z");

    private final UserRepository users = mock(UserRepository.class);
    private final AuditLogRepository audits = mock(AuditLogRepository.class);
    private final HouseholdWriteLock writeLock = mock(HouseholdWriteLock.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final User user = mock(User.class);
    private final AtomicInteger failedAttempts = new AtomicInteger();
    private final AtomicReference<OffsetDateTime> lockedUntil = new AtomicReference<>();
    private AuthService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-11-14T04:30:00Z"), ZoneOffset.UTC);
        service = new AuthService(users, audits, writeLock, passwordEncoder, clock);
        when(user.getId()).thenReturn(7L);
        when(user.getUsername()).thenReturn("operator");
        when(user.getRole()).thenReturn(UserRole.ROLE_ADMIN);
        when(user.getPasswordHash()).thenReturn("encoded");
        when(user.isActive()).thenReturn(true);
        when(user.getFailedAttempts()).thenAnswer(invocation -> failedAttempts.get());
        when(user.getLockedUntil()).thenAnswer(invocation -> lockedUntil.get());
        doAnswer(invocation -> {
            failedAttempts.set(invocation.getArgument(0));
            lockedUntil.set(invocation.getArgument(1));
            return null;
        }).when(user).updateLoginFailureState(anyInt(), nullable(OffsetDateTime.class));
        doAnswer(invocation -> {
            failedAttempts.set(0);
            lockedUntil.set(null);
            return null;
        }).when(user).resetLoginFailureState();
        when(users.existsByUsername("operator")).thenReturn(true);
        when(users.findByUsernameForUpdate("operator")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
    }

    @Test
    void fifthConsecutiveFailureLocksForExactlyFifteenMinutes() {
        failedAttempts.set(4);

        var result = service.authenticate(" Operator ", "wrong", null);

        assertEquals(AuthService.LoginStatus.LOCKED_NOW, result.status());
        assertEquals(5, failedAttempts.get());
        assertEquals(NOW.plusMinutes(15), lockedUntil.get());
        verify(audits).save(argThat(audit -> "LOGIN_FAILURE".equals(audit.getAction())));
        InOrder lockOrder = inOrder(users, writeLock);
        lockOrder.verify(users).existsByUsername("operator");
        lockOrder.verify(writeLock).acquire();
        lockOrder.verify(users).findByUsernameForUpdate("operator");
    }

    @Test
    void requestDuringLockDoesNotCheckPasswordOrExtendLock() {
        OffsetDateTime originalExpiry = NOW.plusMinutes(7);
        failedAttempts.set(5);
        lockedUntil.set(originalExpiry);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(AuthService.LoginStatus.ACCOUNT_LOCKED, result.status());
        assertEquals(originalExpiry, lockedUntil.get());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(user, never()).updateLoginFailureState(anyInt(), nullable(OffsetDateTime.class));
    }

    @Test
    void exactExpiryResetsCounterBeforeProcessingNewFailure() {
        failedAttempts.set(5);
        lockedUntil.set(NOW);

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(AuthService.LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(1, failedAttempts.get());
        assertEquals(null, lockedUntil.get());
        verify(user).resetLoginFailureState();
    }

    @Test
    void successfulLoginResetsCounterAndReturnsOnlySafeIdentity() {
        failedAttempts.set(3);
        when(passwordEncoder.matches("correct", "encoded")).thenReturn(true);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(AuthService.LoginStatus.SUCCESS, result.status());
        assertEquals(7L, result.userId());
        assertEquals("operator", result.username());
        assertEquals(UserRole.ROLE_ADMIN, result.role());
        assertEquals(0, failedAttempts.get());
        assertEquals(null, lockedUntil.get());
        verify(audits).save(argThat(audit -> "LOGIN_SUCCESS".equals(audit.getAction())));
    }

    @Test
    void inactiveAndUnknownAccountsAreRejectedAndAuditedWithoutPasswordCheck() {
        when(user.isActive()).thenReturn(false);
        var disabled = service.authenticate("operator", "correct", null);
        assertEquals(AuthService.LoginStatus.DISABLED, disabled.status());

        when(users.existsByUsername("missing")).thenReturn(false);
        var unknown = service.authenticate("missing", "correct", null);
        assertEquals(AuthService.LoginStatus.USER_NOT_FOUND, unknown.status());

        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits, times(2)).save(argThat(audit -> "LOGIN_FAILURE".equals(audit.getAction())));
        verify(writeLock, times(1)).acquire();
    }

    @Test
    void changesPasswordOnlyAfterCurrentPasswordAndConfirmationPass() {
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("current", "encoded")).thenReturn(true);
        when(passwordEncoder.encode("new-password")).thenReturn("new-encoded");

        service.changePassword(7L, "current", "new-password", "new-password", null);

        InOrder lockOrder = inOrder(writeLock, users);
        lockOrder.verify(writeLock).acquire();
        lockOrder.verify(users).findById(7L);
        verify(user).changePasswordHash("new-encoded");
        verify(audits).save(argThat(audit -> "PASSWORD_CHANGE".equals(audit.getAction())));
    }

    @Test
    void rejectsPasswordChangeWhenCurrentPasswordOrConfirmationIsWrong() {
        when(users.findById(7L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        IllegalArgumentException currentPasswordError = assertThrows(
                IllegalArgumentException.class,
                () -> service.changePassword(7L, "wrong", "new-password", "new-password", null));
        assertEquals("Current password is incorrect.", currentPasswordError.getMessage());

        when(passwordEncoder.matches("current", "encoded")).thenReturn(true);
        IllegalArgumentException confirmationError = assertThrows(
                IllegalArgumentException.class,
                () -> service.changePassword(7L, "current", "new-password", "different", null));
        assertEquals("New password confirmation does not match.", confirmationError.getMessage());
        verify(user, never()).changePasswordHash(any());
        verifyNoInteractions(audits);
    }
}
