package com.bluemoon.bfms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Hồ sơ định danh dùng chung trong bảng {@code persons}.
 *
 * <p>Thông tin cá nhân được lưu một lần để tài khoản, hộ gia đình và cư trú
 * cùng tham chiếu bằng khóa ngoại. CCCD và điện thoại là {@code String} để
 * giữ số 0 ở đầu; các giá trị tùy chọn không có dữ liệu được biểu diễn bằng
 * {@code null}, không thay bằng chuỗi rỗng.</p>
 */
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Họ và tên không được để trống.")
    @Size(max = 100, message = "Họ và tên không được dài quá 100 ký tự.")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Pattern(regexp = "[0-9]{12}", message = "CCCD phải gồm đúng 12 chữ số.")
    @Column(name = "national_id", length = 12)
    private String nationalId;

    @Pattern(regexp = "[0-9]{10}", message = "Số điện thoại phải gồm đúng 10 chữ số.")
    @Column(name = "phone", length = 20)
    private String phone;

    @Pattern(regexp = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+", message = "Email không đúng định dạng.")
    @Size(max = 255, message = "Email không được dài quá 255 ký tự.")
    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Person() {
    }

    /**
     * Tạo hồ sơ cá nhân cơ bản; ngày sinh và giới tính có thể được bổ sung sau.
     *
     * @param fullName họ và tên
     * @param nationalId CCCD gồm 12 chữ số, hoặc {@code null} nếu chưa có
     * @param phone số điện thoại gồm 10 chữ số, hoặc {@code null} nếu chưa có
     * @param email email tùy chọn
     */
    public Person(String fullName, String nationalId, String phone, String email) {
        updateHeadProfile(fullName, nationalId, phone, email);
    }

    /**
     * Thay đổi các trường hồ sơ được dùng trong biểu mẫu chủ hộ.
     *
     * @param fullName họ và tên mới
     * @param nationalId CCCD mới hoặc {@code null}
     * @param phone số điện thoại mới hoặc {@code null}
     * @param email email mới hoặc {@code null}
     */
    public void updateHeadProfile(String fullName, String nationalId, String phone, String email) {
        updatePersonalDetails(fullName, nationalId, phone, email, dateOfBirth, gender);
    }

    /**
     * Cập nhật các trường hồ sơ cá nhân được lưu trong bảng persons.
     *
     * @param fullName họ và tên
     * @param nationalId CCCD hoặc null nếu chưa có
     * @param phone số điện thoại hoặc null nếu chưa có
     * @param email email hoặc null nếu chưa có
     * @param dateOfBirth ngày sinh hoặc null nếu chưa khai báo
     * @param gender giới tính hoặc null nếu chưa khai báo
     */
    public void updatePersonalDetails(String fullName, String nationalId, String phone, String email,
            LocalDate dateOfBirth, Gender gender) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    /** @return khóa chính của hồ sơ cá nhân */
    public Long getId() {
        return id;
    }

    /** @return họ và tên */
    public String getFullName() {
        return fullName;
    }

    /** @return CCCD hoặc {@code null} nếu chưa có */
    public String getNationalId() {
        return nationalId;
    }

    /** @return số điện thoại hoặc {@code null} nếu chưa có */
    public String getPhone() {
        return phone;
    }

    /** @return email hoặc {@code null} nếu chưa có */
    public String getEmail() {
        return email;
    }

    /** @return ngày sinh hoặc {@code null} nếu chưa được khai báo */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /** @return giới tính hoặc {@code null} nếu chưa được khai báo */
    public Gender getGender() {
        return gender;
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
