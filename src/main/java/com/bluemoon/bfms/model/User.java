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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

/**
 * Ánh xạ tài khoản đăng nhập trong bảng {@code users}.
 *
 * <p>Tài khoản tách biệt với hồ sơ cá nhân: {@code person_id} là khóa ngoại
 * một-một tới {@link Person}, còn tên/email/điện thoại được đọc qua hồ sơ đó.
 * Hash mật khẩu, bộ đếm đăng nhập sai và thời điểm khóa là dữ liệu nội bộ;
 * không đưa trực tiếp các trường này vào DTO hoặc giao diện.</p>
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Size(max = 50, message = "Tên đăng nhập không được dài quá 50 ký tự.")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank(message = "Hash mật khẩu không được để trống.")
    @Size(max = 255, message = "Hash mật khẩu không được dài quá 255 ký tự.")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotNull(message = "Tài khoản phải liên kết với một hồ sơ cá nhân.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @NotNull(message = "Vai trò không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Min(value = 0, message = "Số lần đăng nhập sai không được âm.")
    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected User() {
    }

    /**
     * Tạo tài khoản mới với bộ đếm sai bằng 0 và trạng thái đang hoạt động.
     *
     * @param username tên đăng nhập đã chuẩn hóa thành chữ thường
     * @param passwordHash hash BCrypt; không truyền mật khẩu thô vào entity
     * @param person hồ sơ cá nhân liên kết duy nhất với tài khoản
     * @param role vai trò hệ thống của tài khoản
     */
    public User(String username, String passwordHash, Person person, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.person = person;
        this.role = role;
        this.failedAttempts = 0;
        this.active = true;
    }

    /**
     * Thay đổi tên đăng nhập, vai trò và trạng thái tài khoản.
     *
     * @param username tên đăng nhập mới đã chuẩn hóa
     * @param role vai trò mới
     * @param active true nếu tài khoản được phép đăng nhập
     */
    public void updateAccount(String username, UserRole role, boolean active) {
        this.username = username;
        this.role = role;
        this.active = active;
    }

    /**
     * Thay hash sau khi Service đã xác minh và băm mật khẩu bằng BCrypt.
     *
     * @param passwordHash hash mới, không phải mật khẩu thô
     */
    public void changePasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Lưu trạng thái đăng nhập sai đã được Service tính toán.
     *
     * @param failedAttempts số lần sai liên tiếp, không âm
     * @param lockedUntil thời điểm hết khóa hoặc null nếu tài khoản không bị khóa
     */
    public void updateLoginFailureState(int failedAttempts, OffsetDateTime lockedUntil) {
        this.failedAttempts = failedAttempts;
        this.lockedUntil = lockedUntil;
    }

    /** Đặt lại bộ đếm và thời điểm khóa sau khi đăng nhập hợp lệ/hết hạn khóa. */
    public void resetLoginFailureState() {
        this.failedAttempts = 0;
        this.lockedUntil = null;
    }

    /** @return khóa chính tài khoản */
    public Long getId() {
        return id;
    }

    /** @return tên đăng nhập đã chuẩn hóa */
    public String getUsername() {
        return username;
    }

    /**
     * @return hash mật khẩu nội bộ; tuyệt đối không đưa trực tiếp ra giao diện
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /** @return hồ sơ cá nhân liên kết với tài khoản */
    public Person getPerson() {
        return person;
    }

    /** @return vai trò được lưu theo mã enum */
    public UserRole getRole() {
        return role;
    }

    /** @return số lần đăng nhập sai liên tiếp */
    public int getFailedAttempts() {
        return failedAttempts;
    }

    /** @return thời điểm kết thúc khóa tạm thời hoặc {@code null} */
    public OffsetDateTime getLockedUntil() {
        return lockedUntil;
    }

    /** @return true nếu tài khoản đang được kích hoạt */
    public boolean isActive() {
        return active;
    }

    /** @return thời điểm tạo do database ghi nhận */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /** @return thời điểm cập nhật gần nhất do database ghi nhận */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
