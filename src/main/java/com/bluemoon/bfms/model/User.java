package com.bluemoon.bfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetDateTime;
import java.util.Locale;

/** JPA entity của tài khoản; chỉ lưu hash mật khẩu, không lưu mật khẩu thô. */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "users_username_key", columnNames = "username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.Generated(event = {
            org.hibernate.generator.EventType.INSERT,
            org.hibernate.generator.EventType.UPDATE
    })
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected User() {
    }

    public User(
            Long id,
            String username,
            String passwordHash,
            Person person,
            UserRole role,
            int failedAttempts,
            OffsetDateTime lockedUntil,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.person = person;
        this.role = role;
        this.failedAttempts = failedAttempts;
        this.lockedUntil = lockedUntil;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() { return id; }
    public String username() { return username; }
    public String passwordHash() { return passwordHash; }
    public Person person() { return person; }
    public Long personId() { return person == null ? null : person.id(); }
    public UserRole role() { return role; }
    public int failedAttempts() { return failedAttempts; }
    public OffsetDateTime lockedUntil() { return lockedUntil; }
    public boolean active() { return active; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    /**
     * Kiểm tra các trường tài khoản có thể xác thực ngay trên đối tượng.
     *
     * <p>Username phải viết thường, đã trim và tối đa 50 ký tự; chuỗi mật khẩu
     * phải là hash không rỗng; ID cá nhân phải dương; vai trò phải được gán;
     * số lần đăng nhập sai không được âm. Phương thức không xác minh username
     * duy nhất, hash có đúng thuật toán hay người liên kết có tồn tại không.</p>
     *
     * Có thể ném IllegalArgumentException nếu một trường vi phạm điều kiện cục bộ
     */
    public void validate() {
        if (username == null || username.isBlank() || username.length() > 50
                || !username.equals(username.trim().toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("username must be trimmed, lowercase, and at most 50 characters");
        }
        if (passwordHash == null || passwordHash.isBlank() || passwordHash.length() > 255) {
            throw new IllegalArgumentException("passwordHash must be non-blank and at most 255 characters");
        }
        if (person == null || person.id() == null || person.id() <= 0) {
            throw new IllegalArgumentException("personId must be positive");
        }
        if (role == null) {
            throw new IllegalArgumentException("role is required");
        }
        if (failedAttempts < 0) {
            throw new IllegalArgumentException("failedAttempts must not be negative");
        }
    }

    /**
     * Xác định tài khoản có đang trong thời gian khóa tại thời điểm được truyền vào hay không.
     *
     * <p>Thời điểm đúng bằng lockedUntil được xem là đã hết khóa; thời
     * điểm sớm hơn mới được xem là đang khóa. Trạng thái kích hoạt không ảnh
     * hưởng đến kết quả của phương thức này.</p>
     *
     * Tham số now: thời điểm cần kiểm tra, không được null
     * Kết quả: true nếu có thời hạn khóa và now còn trước thời hạn đó
     * Có thể ném IllegalArgumentException nếu now là null
     */
    public boolean isLockedAt(OffsetDateTime now) {
        requireTime(now);
        return lockedUntil != null && now.isBefore(lockedUntil);
    }

    /**
     * Kiểm tra điều kiện trạng thái tối thiểu để cho phép tiếp tục xác thực.
     *
     * <p>Tài khoản phải đang hoạt động và không còn trong thời hạn khóa.
     * Đây không phải thao tác xác minh mật khẩu, cấp session hay phân quyền.</p>
     *
     * Tham số now: thời điểm cần kiểm tra, không được null
     * Kết quả: true nếu tài khoản đang hoạt động và không bị khóa tại now
     * Có thể ném IllegalArgumentException nếu now là null
     */
    public boolean canAuthenticateAt(OffsetDateTime now) {
        return active && !isLockedAt(now);
    }

    /**
     * Tạo trạng thái tài khoản mới sau một lần nhập thông tin xác thực sai.
     *
     * <p>Mỗi lần sai liên tiếp tăng bộ đếm lên một. Từ lần sai thứ năm, đặt
     * thời hạn khóa thành now + 15 phút. Nếu lần khóa trước đã hết hạn,
     * bộ đếm được bắt đầu lại từ một. Record hiện tại không bị thay đổi; service
     * cần lưu entity mới trong giao dịch phù hợp.</p>
     *
     * Tham số now: thời điểm ghi nhận lần xác thực sai, không được null
     * Kết quả: bản sao có bộ đếm và thời hạn khóa mới
     * Có thể ném IllegalArgumentException nếu now là null
     * Có thể ném IllegalStateException nếu tài khoản không hoạt động hoặc vẫn đang bị khóa
     */
    public User recordFailedLogin(OffsetDateTime now) {
        requireTime(now);
        if (!active) {
            throw new IllegalStateException("An inactive account cannot record a login attempt");
        }
        if (isLockedAt(now)) {
            throw new IllegalStateException("A locked account cannot record a login attempt");
        }

        int attempts = lockedUntil != null ? 0 : failedAttempts;
        int updatedAttempts = Math.addExact(attempts, 1);
        OffsetDateTime updatedLock = updatedAttempts >= MAX_FAILED_ATTEMPTS
                ? now.plusMinutes(LOCK_DURATION_MINUTES)
                : null;
        return new User(id, username, passwordHash, person, role, updatedAttempts,
                updatedLock, active, createdAt, updatedAt);
    }

    /**
     * Tạo trạng thái tài khoản sau khi xác thực thành công.
     *
     * <p>Xóa số lần đăng nhập sai liên tiếp và thời hạn khóa đang lưu, nhưng
     * không tự bật tài khoản đang bị vô hiệu hóa. Service vẫn phải tự xác minh
     * mật khẩu trước khi gọi phương thức này.</p>
     *
     * Kết quả: bản sao có failedAttempts = 0 và lockedUntil = null
     */
    public User recordSuccessfulLogin() {
        return new User(id, username, passwordHash, person, role, 0,
                null, active, createdAt, updatedAt);
    }

    /**
     * Xóa trạng thái khóa và bộ đếm nếu thời hạn khóa đã kết thúc.
     *
     * <p>Thời điểm bằng thời hạn hết khóa được xem là đã hết hạn. Nếu khóa
     * chưa hết hoặc chưa từng có, phương thức trả lại chính đối tượng hiện tại.</p>
     *
     * Tham số now: thời điểm đánh giá, không được null
     * Kết quả: bản sao đã xóa khóa và bộ đếm, hoặc chính đối tượng nếu chưa hết khóa
     * Có thể ném IllegalArgumentException nếu now là null
     */
    public User clearExpiredLock(OffsetDateTime now) {
        requireTime(now);
        if (lockedUntil != null && !now.isBefore(lockedUntil)) {
            return recordSuccessfulLogin();
        }
        return this;
    }

    /**
     * Tạo bản sao với trạng thái hoạt động mới.
     *
     * <p>Thao tác này chỉ thay đổi cờ active; không tự xóa bộ đếm,
     * thời hạn khóa hay thay đổi vai trò.</p>
     *
     * Tham số enabled: true để bật tài khoản, false để vô hiệu hóa
     * Kết quả: bản sao tài khoản có trạng thái mới
     */
    public User withActive(boolean enabled) {
        return new User(id, username, passwordHash, person, role, failedAttempts,
                lockedUntil, enabled, createdAt, updatedAt);
    }

    /**
     * Tạo bản sao được gán vai trò mới.
     *
     * <p>Phương thức chỉ thay đổi dữ liệu vai trò. Việc người thực hiện có
     * quyền cấp vai trò đó hay không phải được kiểm tra ở tầng service/bảo mật.</p>
     *
     * Tham số newRole: vai trò mới, không được null
     * Kết quả: bản sao tài khoản có vai trò mới
     * Có thể ném IllegalArgumentException nếu newRole là null
     */
    public User withRole(UserRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("role is required");
        }
        return new User(id, username, passwordHash, person, newRole, failedAttempts,
                lockedUntil, active, createdAt, updatedAt);
    }

    /**
     * Tạo bản sao với chuỗi mật khẩu đã mã hóa mới.
     *
     * <p>Đầu vào phải là kết quả mã hóa, tuyệt đối không truyền mật khẩu thô.
     * Phương thức chỉ kiểm tra chuỗi không rỗng và giới hạn độ dài; thuật toán
     * mã hóa và chính sách mật khẩu thuộc trách nhiệm của service.</p>
     *
     * Tham số newPasswordHash: hash mới, không được rỗng và tối đa 255 ký tự
     * Kết quả: bản sao tài khoản chứa hash mới
     * Có thể ném IllegalArgumentException nếu hash rỗng hoặc dài hơn 255 ký tự
     */
    public User withPasswordHash(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank() || newPasswordHash.length() > 255) {
            throw new IllegalArgumentException("passwordHash must be non-blank and at most 255 characters");
        }
        return new User(id, username, newPasswordHash, person, role, failedAttempts,
                lockedUntil, active, createdAt, updatedAt);
    }

    /**
     * Tạo bản sao có username đã trim và chuyển thành chữ thường.
     *
     * <p>Phương thức không kiểm tra username rỗng, chiều dài hay tính duy nhất;
     * hãy gọi validate() sau khi chuẩn hóa trước khi lưu.</p>
     *
     * Kết quả: bản sao với username đã chuẩn hóa; nếu username ban đầu là null, giá trị vẫn là null
     */
    public User normalized() {
        String normalizedUsername = username == null ? null : username.trim().toLowerCase(Locale.ROOT);
        return new User(id, normalizedUsername, passwordHash, person, role, failedAttempts,
                lockedUntil, active, createdAt, updatedAt);
    }

    /**
     * Đảm bảo thời điểm đầu vào có giá trị trước khi tính trạng thái khóa.
     *
     * Tham số now: thời điểm cần kiểm tra
     * Có thể ném IllegalArgumentException nếu thời điểm là null
     */
    private static void requireTime(OffsetDateTime now) {
        if (now == null) {
            throw new IllegalArgumentException("now is required");
        }
    }
}
