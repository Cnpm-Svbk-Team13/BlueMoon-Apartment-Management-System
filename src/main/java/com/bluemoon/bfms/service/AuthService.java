package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.AuditLog;
import com.bluemoon.bfms.model.User;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Objects;

/**
 * Xử lý xác thực, khóa đăng nhập và đổi mật khẩu theo US-SEC-02 / NFR-02.
 *
 * <p>Service điều phối Repository, kiểm tra BCrypt và cập nhật trạng thái tài khoản;
 * Controller chỉ nên nhận kết quả này để tạo phiên hoặc hiển thị thông báo chung.</p>
 */
@Service
public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private final UserRepository users;
    private final AuditLogRepository audits;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    /**
     * Constructor Spring sử dụng. BCrypt được cố định ở mức 10 rounds theo AC;
     * Clock UTC giúp mốc khóa không phụ thuộc múi giờ máy chạy ứng dụng.
     */
    @Autowired
    public AuthService(UserRepository users, AuditLogRepository audits) {
        this(users, audits, new BCryptPasswordEncoder(10), Clock.systemUTC());
    }

    /** Constructor package-private để test có thể thay PasswordEncoder và thời gian. */
    AuthService(UserRepository users, AuditLogRepository audits, PasswordEncoder passwordEncoder, Clock clock) {
        this.users = users;
        this.audits = audits;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    /**
     * Kiểm tra thông tin đăng nhập và cập nhật khóa/audit trong cùng transaction.
     * Khi trả về kết quả thất bại, transaction vẫn commit để giữ bộ đếm và audit;
     * tầng web phải ánh xạ mọi trạng thái không thành công sang cùng một thông báo.
     *
     * @param username tên đăng nhập do người dùng nhập
     * @param rawPassword mật khẩu thô; chỉ dùng để so khớp, không lưu hoặc ghi audit
     * @param ipAddress địa chỉ IP client tùy chọn
     * @return kết quả nội bộ cho tầng xác thực, không bao giờ chứa password hash
     * @throws IllegalArgumentException nếu IP vượt giới hạn lưu trữ
     */
    // @Transactional tương đương BEGIN/COMMIT; rollback cả cập nhật User và audit nếu có exception.
    @Transactional(rollbackFor = Exception.class)
    public AuthenticationResult authenticate(String username, String rawPassword, String ipAddress) {
        validateIpAddress(ipAddress);
        String normalizedUsername = normalizeUsername(username);
        if (normalizedUsername == null) {
            // Không có username hợp lệ để tra cứu; vẫn ghi nhận lần thất bại nhưng không lưu đầu vào.
            auditFailure(null, ipAddress);
            return AuthenticationResult.failed(LoginStatus.USER_NOT_FOUND);
        }

        // PESSIMISTIC_WRITE khóa hàng đến hết transaction; các lần thử đồng thời lần lượt cập nhật bộ đếm.
        User user = users.findByUsernameForUpdate(normalizedUsername).orElse(null);
        if (user == null) {
            // Ghi audit với tác nhân null vì không tìm được tài khoản tương ứng.
            auditFailure(null, ipAddress);
            return AuthenticationResult.failed(LoginStatus.USER_NOT_FOUND);
        }
        if (!user.isActive()) {
            // Tài khoản bị vô hiệu hóa luôn bị chặn, không phụ thuộc mật khẩu hoặc thời gian khóa.
            auditFailure(user, ipAddress);
            return AuthenticationResult.failed(LoginStatus.DISABLED);
        }

        OffsetDateTime now = OffsetDateTime.now(clock);
        OffsetDateTime lockedUntil = user.getLockedUntil();
        if (lockedUntil != null && !now.isBefore(lockedUntil)) {
            // Dùng điều kiện now >= lockedUntil: tại đúng thời điểm hết hạn cũng phải mở khóa.
            user.resetLoginFailureState();
        } else if (lockedUntil != null) {
            // Không so khớp mật khẩu và không kéo dài lockedUntil khi tài khoản vẫn đang bị khóa.
            auditFailure(user, ipAddress);
            return AuthenticationResult.failed(LoginStatus.ACCOUNT_LOCKED);
        }

        if (rawPassword == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            int failedAttempts = user.getFailedAttempts() + 1;
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                // Lần sai thứ năm đặt mốc khóa một lần; những yêu cầu sau bị chặn ở nhánh trên.
                user.updateLoginFailureState(failedAttempts, now.plusMinutes(LOCK_MINUTES));
                auditFailure(user, ipAddress);
                return AuthenticationResult.failed(LoginStatus.LOCKED_NOW);
            }
            user.updateLoginFailureState(failedAttempts, null);
            auditFailure(user, ipAddress);
            return AuthenticationResult.failed(LoginStatus.BAD_CREDENTIALS);
        }

        // Đăng nhập đúng kết thúc chuỗi thất bại liên tiếp và ghi audit trước khi transaction commit.
        user.resetLoginFailureState();
        audits.save(new AuditLog(user, "LOGIN_SUCCESS", "User", user.getId(), null, ipAddress));
        return AuthenticationResult.success(user);
    }

    /**
     * Đổi mật khẩu sau khi xác minh mật khẩu hiện tại của tài khoản đã xác thực.
     *
     * @param userId ID tài khoản lấy từ principal phía server, không lấy từ request
     * @param currentPassword mật khẩu hiện tại cần xác minh
     * @param newPassword mật khẩu mới cần băm trước khi lưu
     * @param confirmation giá trị nhập lại để xác nhận mật khẩu mới
     * @param ipAddress địa chỉ IP client tùy chọn
     * @throws IllegalArgumentException nếu tài khoản, mật khẩu hoặc IP không hợp lệ
     */
    // Cập nhật hash và audit như một đơn vị: lỗi lưu audit cũng làm rollback thay đổi mật khẩu.
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(
            Long userId, String currentPassword, String newPassword, String confirmation, String ipAddress) {
        validateIpAddress(ipAddress);
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("An authenticated account is required.");
        }
        User user = users.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account does not exist."));
        if (!user.isActive()) {
            throw new IllegalArgumentException("The account is inactive.");
        }
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        validateNewPassword(newPassword);
        if (!Objects.equals(newPassword, confirmation)) {
            throw new IllegalArgumentException("New password confirmation does not match.");
        }

        user.changePasswordHash(passwordEncoder.encode(newPassword));
        audits.save(new AuditLog(user, "PASSWORD_CHANGE", "User", user.getId(), null, ipAddress));
    }

    private void auditFailure(User user, String ipAddress) {
        // Không ghi username hoặc mật khẩu vào nhật ký để tránh lưu dữ liệu đăng nhập không cần thiết.
        audits.save(new AuditLog(user, "LOGIN_FAILURE", "User",
                user == null ? null : user.getId(), null, ipAddress));
    }

    private void validateNewPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("New password must contain at least 8 characters.");
        }
        // BCrypt chỉ nhận tối đa 72 byte; tính theo UTF-8 để cả ký tự nhiều byte cũng được chặn rõ ràng.
        if (password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("New password exceeds the BCrypt input limit.");
        }
    }

    private void validateIpAddress(String ipAddress) {
        if (ipAddress != null && ipAddress.length() > 50) {
            throw new IllegalArgumentException("Client IP address must contain at most 50 characters.");
        }
    }

    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return username.strip().toLowerCase(Locale.ROOT);
    }

    /** Kết quả nội bộ; Controller phải dùng chung một thông báo cho mọi trạng thái thất bại. */
    public enum LoginStatus {
        SUCCESS,
        USER_NOT_FOUND,
        DISABLED,
        ACCOUNT_LOCKED,
        LOCKED_NOW,
        BAD_CREDENTIALS
    }

    /**
     * Danh tính tối thiểu để tầng xác thực tạo Authentication/session;
     * không giữ tham chiếu Entity User nên không thể vô tình trả password hash ra giao diện.
     */
    public record AuthenticationResult(LoginStatus status, Long userId, String username, UserRole role) {
        private static AuthenticationResult failed(LoginStatus status) {
            return new AuthenticationResult(status, null, null, null);
        }

        private static AuthenticationResult success(User user) {
            return new AuthenticationResult(LoginStatus.SUCCESS, user.getId(), user.getUsername(), user.getRole());
        }
    }
}
