package com.bluemoon.bfms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.UserRepository;
import com.bluemoon.bfms.service.AuthService.LoginStatus;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Issue #16 (QA-02) - CFG Basis Path cho {@code AuthService.authenticate}.
 *
 * <p>
 * CFG: 8 điểm quyết định ⇒ V(G) = 9
 * (11 nếu đếm từng điều kiện trong {@code &&} / {@code ||}; hai điều kiện đó
 * được phủ bằng
 * BP1/BP6/BP7 và EDGE_09). Quyết định:
 * 
 * <pre>
 *  D1 username null/blank?        D5 lockedUntil != null && now >= lockedUntil ? (mở khóa)
 *  D2 !existsByUsername?          D6 lockedUntil != null ? (còn khóa)
 *  D3 findForUpdate rỗng (race)?  D7 rawPassword == null || !matches ?
 *  D4 !user.isActive?             D8 failedAttempts+1 >= 5 ?
 * </pre>
 * 
 * {@code validateIpAddress} là hàm riêng (V(G)=3) — phủ bằng EDGE_10a/10b.
 *
 * <p>
 * {@code ./mvnw -Dtest=AuthServiceBasisPathTest test}.
 */
@DisplayName("Issue #16 - AuthService.authenticate: 9 basis path + ca biên")
class AuthServiceBasisPathTest {
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
        when(user.getFailedAttempts()).thenAnswer(i -> failedAttempts.get());
        when(user.getLockedUntil()).thenAnswer(i -> lockedUntil.get());
        doAnswer(i -> {
            failedAttempts.set(i.getArgument(0));
            lockedUntil.set(i.getArgument(1));
            return null;
        }).when(user).updateLoginFailureState(anyInt(), nullable(OffsetDateTime.class));
        doAnswer(i -> {
            failedAttempts.set(0);
            lockedUntil.set(null);
            return null;
        }).when(user).resetLoginFailureState();
        when(users.existsByUsername("operator")).thenReturn(true);
        when(users.findByUsernameForUpdate("operator")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        when(passwordEncoder.matches("correct", "encoded")).thenReturn(true);
    }

    // =====================================================================
    // 9 BASIS PATH (BP1 = đường chuẩn)
    // =====================================================================

    @Test
    @DisplayName("BP1 | TC_LOGIN_BP_05 | Thành công, chuẩn hóa username, reset bộ đếm")
    void bp1_success_resetsCounterAndReturnsSafeIdentity() {
        failedAttempts.set(2);

        var result = service.authenticate(" Operator ", "correct", "10.0.0.1");

        assertEquals(LoginStatus.SUCCESS, result.status());
        assertEquals(7L, result.userId());
        assertEquals("operator", result.username());
        assertEquals(UserRole.ROLE_ADMIN, result.role());
        assertEquals(0, failedAttempts.get());
        assertNull(lockedUntil.get());
        verify(writeLock, times(1)).acquire();
        verify(audits).save(argThat(a -> "LOGIN_SUCCESS".equals(a.getAction())));
    }

    @ParameterizedTest(name = "BP2 | TC_LOGIN_BP_01a | username=[{0}] → USER_NOT_FOUND, không tra cứu")
    @NullAndEmptySource
    @ValueSource(strings = { "   ", "\t" })
    void bp2_blankOrNullUsername_isRejectedWithoutLookup(String username) {
        var result = service.authenticate(username, "correct", null);

        assertEquals(LoginStatus.USER_NOT_FOUND, result.status());
        assertNull(result.userId());
        verify(users, never()).existsByUsername(any());
        verify(users, never()).findByUsernameForUpdate(any());
        verify(writeLock, never()).acquire();
        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP3 | TC_LOGIN_BP_01 | Username không tồn tại")
    void bp3_unknownUser_isRejectedBeforeLock() {
        when(users.existsByUsername("ghost")).thenReturn(false);

        var result = service.authenticate("ghost", "correct", null);

        assertEquals(LoginStatus.USER_NOT_FOUND, result.status());
        assertNull(result.userId());
        assertNull(result.username());
        assertNull(result.role());
        verify(writeLock, never()).acquire();
        verify(users, never()).findByUsernameForUpdate(any());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP4 | TC_LOGIN_BP_06 | Tài khoản biến mất sau khi lấy khóa (race)")
    void bp4_userVanishesAfterLock_isUserNotFound() {
        when(users.existsByUsername("racer")).thenReturn(true);
        when(users.findByUsernameForUpdate("racer")).thenReturn(Optional.empty());

        var result = service.authenticate("racer", "correct", null);

        assertEquals(LoginStatus.USER_NOT_FOUND, result.status());
        verify(writeLock, times(1)).acquire();
        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP5 | TC_LOGIN_EDGE_01 | Vô hiệu hóa: mật khẩu đúng vẫn bị chặn")
    void bp5_disabledAccount_isBlockedWithoutPasswordCheck() {
        when(user.isActive()).thenReturn(false);
        failedAttempts.set(2);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.DISABLED, result.status());
        assertNull(result.userId());
        assertEquals(2, failedAttempts.get());
        assertNull(lockedUntil.get());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP6 | TC_LOGIN_BP_02 | Đang khóa: không so mật khẩu, không kéo dài khóa")
    void bp6_activeLock_blocksEvenWithCorrectPassword() {
        OffsetDateTime expiry = NOW.plusMinutes(10);
        failedAttempts.set(5);
        lockedUntil.set(expiry);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.ACCOUNT_LOCKED, result.status());
        assertEquals(expiry, lockedUntil.get());
        assertEquals(5, failedAttempts.get());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP7 | TC_LOGIN_EDGE_04 | Hết khóa + mật khẩu đúng: mở khóa và thành công")
    void bp7_expiredLockWithCorrectPassword_unlocksAndSucceeds() {
        failedAttempts.set(5);
        lockedUntil.set(NOW.minusSeconds(1));

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.SUCCESS, result.status());
        assertEquals(0, failedAttempts.get());
        assertNull(lockedUntil.get());
        // verify(user).resetLoginFailureState();
        verify(audits).save(argThat(a -> "LOGIN_SUCCESS".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP8 | TC_LOGIN_BP_04 | Sai dưới ngưỡng: BAD_CREDENTIALS, tăng bộ đếm")
    void bp8_wrongPasswordUnderLimit_incrementsCounterWithoutLock() {
        failedAttempts.set(1);

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(2, failedAttempts.get());
        assertNull(lockedUntil.get());
        verify(audits).save(argThat(a -> "LOGIN_FAILURE".equals(a.getAction())));
    }

    @Test
    @DisplayName("BP9 | TC_LOGIN_BP_03 | Sai lần thứ 5: LOCKED_NOW, khóa đúng 15 phút")
    void bp9_fifthFailure_locksForFifteenMinutes() {
        failedAttempts.set(4);

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(LoginStatus.LOCKED_NOW, result.status());
        assertEquals(5, failedAttempts.get());
        assertEquals(NOW.plusMinutes(15), lockedUntil.get());
    }

    // =====================================================================
    // CA BIÊN / ĐIỀU KIỆN (không thêm basis path)
    // =====================================================================

    @Test
    @DisplayName("EDGE_02 | now = lockedUntil − 1s: vẫn khóa, không đổi trạng thái")
    void edge02_oneSecondBeforeExpiry_stillLocked() {
        OffsetDateTime expiry = NOW.plusSeconds(1);
        failedAttempts.set(5);
        lockedUntil.set(expiry);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.ACCOUNT_LOCKED, result.status());
        assertEquals(expiry, lockedUntil.get());
        assertEquals(5, failedAttempts.get());
    }

    @Test
    @DisplayName("EDGE_03 | now == lockedUntil, sai mật khẩu: mở khóa rồi bộ đếm = 1")
    void edge03_exactlyAtExpiry_wrongPassword_restartsCounterAtOne() {
        failedAttempts.set(5);
        lockedUntil.set(NOW);

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(1, failedAttempts.get());
        assertNull(lockedUntil.get());
    }

    @Test
    @DisplayName("EDGE_05 | Sau hết khóa 1s, sai mật khẩu: bộ đếm = 1")
    void edge05_afterExpiry_wrongPassword_restartsCounterAtOne() {
        failedAttempts.set(5);
        lockedUntil.set(NOW.minusSeconds(1));

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(1, failedAttempts.get());
        assertNull(lockedUntil.get());
    }

    @Test
    @DisplayName("EDGE_06 | Đã sai 4 lần rồi nhập đúng: thành công, không khóa")
    void edge06_correctPasswordAtFourFailures_succeedsAndResets() {
        failedAttempts.set(4);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.SUCCESS, result.status());
        assertEquals(0, failedAttempts.get());
        assertNull(lockedUntil.get());
    }

    @Test
    @DisplayName("EDGE_07 | Vừa vô hiệu hóa vừa đang khóa: DISABLED được ưu tiên")
    void edge07_disabledTakesPriorityOverLock() {
        OffsetDateTime expiry = NOW.plusMinutes(5);
        when(user.isActive()).thenReturn(false);
        failedAttempts.set(5);
        lockedUntil.set(expiry);

        var result = service.authenticate("operator", "correct", null);

        assertEquals(LoginStatus.DISABLED, result.status());
        assertEquals(expiry, lockedUntil.get());
    }

    @Test
    @DisplayName("EDGE_08 | Sai lần thứ 4 (3→4): chưa khóa")
    void edge08_fourthFailure_doesNotLock() {
        failedAttempts.set(3);

        var result = service.authenticate("operator", "wrong", null);

        assertEquals(LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(4, failedAttempts.get());
        assertNull(lockedUntil.get());
    }

    @Test
    @DisplayName("EDGE_09 | Mật khẩu null tính là sai, không gọi encoder")
    void edge09_nullPassword_isBadCredentialsWithoutEncoder() {
        failedAttempts.set(0);

        var result = service.authenticate("operator", null, null);

        assertEquals(LoginStatus.BAD_CREDENTIALS, result.status());
        assertEquals(1, failedAttempts.get());
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("EDGE_10a | IP dài 51 ký tự: bị từ chối trước mọi truy cập dữ liệu")
    void edge10a_ipAddressTooLong_isRejectedBeforeAnyAccess() {
        assertThrows(IllegalArgumentException.class,
                () -> service.authenticate("operator", "correct", "x".repeat(51)));

        verifyNoInteractions(users, audits, writeLock, passwordEncoder);
    }

    @Test
    @DisplayName("EDGE_10b | IP dài đúng 50 ký tự: được chấp nhận")
    void edge10b_ipAddressAtLimit_isAccepted() {
        when(users.existsByUsername("ghost")).thenReturn(false);

        var result = service.authenticate("ghost", "correct", "x".repeat(50));

        assertEquals(LoginStatus.USER_NOT_FOUND, result.status());
    }
}
