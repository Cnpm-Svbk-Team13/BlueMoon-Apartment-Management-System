package com.bluemoon.bfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

/** JPA entity cho hồ sơ cá nhân dùng chung; SQL vẫn do PostgreSQL schema quản lý. */
@Entity
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(name = "uq_persons_national_id", columnNames = "national_id")
})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "national_id", length = 12)
    private String nationalId;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.Generated(event = {
            org.hibernate.generator.EventType.INSERT,
            org.hibernate.generator.EventType.UPDATE
    })
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Person() {
    }

    public Person(
            Long id,
            String fullName,
            String nationalId,
            String phone,
            String email,
            LocalDate dateOfBirth,
            Gender gender,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() { return id; }
    public String fullName() { return fullName; }
    public String nationalId() { return nationalId; }
    public String phone() { return phone; }
    public String email() { return email; }
    public LocalDate dateOfBirth() { return dateOfBirth; }
    public Gender gender() { return gender; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");

    /**
     * Tạo một bản sao đã chuẩn hóa dữ liệu văn bản của cá nhân.
     *
     * <p>Họ tên được bỏ khoảng trắng ở hai đầu; CCCD, điện thoại và email
     * tùy chọn được bỏ khoảng trắng ở hai đầu; giá trị tùy chọn rỗng được đổi
     * thành null. Email được chuyển thành chữ thường để phù hợp quy
     * ước lưu trữ. Phương thức trả về entity mới thay vì sửa đối tượng hiện tại.</p>
     *
     * Kết quả: bản sao đã chuẩn hóa; ném IllegalArgumentException nếu họ tên bị thiếu
     */
    public Person normalized() {
        String normalizedEmail = normalizeOptional(email);
        return new Person(
                id,
                trimRequired(fullName, "fullName"),
                normalizeOptional(nationalId),
                normalizeOptional(phone),
                normalizedEmail == null ? null : normalizedEmail.toLowerCase(Locale.ROOT),
                dateOfBirth,
                gender,
                createdAt,
                updatedAt);
    }

    /**
     * Kiểm tra các ràng buộc cục bộ của hồ sơ cá nhân.
     *
     * <p>Họ tên phải có nội dung và không vượt quá 100 ký tự; CCCD nếu có phải
     * gồm 12 chữ số; điện thoại nếu có phải gồm 10 chữ số; email nếu có phải
     * được trim, viết thường và không vượt quá 255 ký tự. Phương thức không
     * kiểm tra tính duy nhất vì việc đó cần truy vấn toàn bộ dữ liệu.</p>
     *
     * Có thể ném IllegalArgumentException nếu trường bắt buộc hoặc định dạng dữ liệu không hợp lệ
     */
    public void validate() {
        requireText(fullName, "fullName", 100);
        requireOptionalPattern(nationalId, "nationalId", NATIONAL_ID_PATTERN);
        requireOptionalPattern(phone, "phone", PHONE_PATTERN);
        requireOptionalLength(email, "email", 255);
        if (email != null && !email.equals(email.trim().toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("email must be trimmed and lowercase");
        }
    }

    /**
     * Kiểm tra hồ sơ có đủ CCCD và điện thoại hợp lệ để làm chủ hộ hay không.
     *
     * <p>Phương thức chỉ xét định dạng hai trường; các điều kiện khác như dữ
     * liệu có tồn tại trong cơ sở dữ liệu hay có bị trùng không thuộc phạm vi
     * kiểm tra của đối tượng này.</p>
     *
     * Kết quả: true nếu CCCD có 12 chữ số và điện thoại có 10 chữ số; ngược lại trả về false
     */
    public boolean hasHouseholdHeadContactDetails() {
        return nationalId != null
                && NATIONAL_ID_PATTERN.matcher(nationalId).matches()
                && phone != null
                && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Kiểm tra các ràng buộc hồ sơ cá nhân và yêu cầu liên hệ của chủ hộ.
     *
     * <p>Trước tiên gọi validate() để kiểm tra các trường thông
     * thường, sau đó yêu cầu CCCD 12 chữ số và điện thoại 10 chữ số.</p>
     *
     * Có thể ném IllegalArgumentException nếu dữ liệu cá nhân không hợp lệ hoặc thiếu CCCD/điện thoại hợp lệ
     */
    public void validateAsHouseholdHead() {
        validate();
        if (!hasHouseholdHeadContactDetails()) {
            throw new IllegalArgumentException(
                    "A household head must have a 12-digit national ID and 10-digit phone number");
        }
    }

    /**
     * Loại bỏ khoảng trắng ở hai đầu của một trường bắt buộc.
     *
     * Tham số value: giá trị cần chuẩn hóa
     * Tham số field: tên trường dùng trong thông báo lỗi
     * Kết quả: giá trị sau khi trim
     * Có thể ném IllegalArgumentException nếu giá trị là null
     */
    private static String trimRequired(String value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    /**
     * Chuẩn hóa trường tùy chọn: giá trị null/trống thành null,
     * các giá trị còn lại được trim.
     *
     * Tham số value: giá trị tùy chọn cần chuẩn hóa
     * Kết quả: giá trị đã chuẩn hóa hoặc null
     */
    private static String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    /**
     * Bảo đảm một trường bắt buộc có nội dung, đã trim và không vượt giới hạn.
     *
     * Tham số value: giá trị cần kiểm tra
     * Tham số field: tên trường dùng trong thông báo lỗi
     * Tham số maxLength: số ký tự tối đa được phép
     * Có thể ném IllegalArgumentException nếu giá trị rỗng, chưa trim hoặc quá dài
     */
    private static void requireText(String value, String field, int maxLength) {
        if (value == null || value.isBlank() || !value.equals(value.trim())) {
            throw new IllegalArgumentException(field + " must be non-blank and trimmed");
        }
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(field + " must not exceed " + maxLength + " characters");
        }
    }

    /**
     * Kiểm tra độ dài và trạng thái trim của trường tùy chọn nếu trường có giá trị.
     *
     * Tham số value: giá trị tùy chọn
     * Tham số field: tên trường dùng trong thông báo lỗi
     * Tham số maxLength: số ký tự tối đa được phép
     * Có thể ném IllegalArgumentException nếu trường có giá trị nhưng rỗng, chưa trim hoặc quá dài
     */
    private static void requireOptionalLength(String value, String field, int maxLength) {
        if (value != null && (value.isBlank() || !value.equals(value.trim())
                || value.length() > maxLength)) {
            throw new IllegalArgumentException(field + " must be blank/null or trimmed and at most "
                    + maxLength + " characters");
        }
    }

    /**
     * Kiểm tra trường tùy chọn có khớp biểu thức chính quy nếu được cung cấp.
     *
     * Tham số value: giá trị cần kiểm tra; null được chấp nhận
     * Tham số field: tên trường dùng trong thông báo lỗi
     * Tham số pattern: mẫu định dạng cần khớp
     * Có thể ném IllegalArgumentException nếu giá trị khác null nhưng sai định dạng
     */
    private static void requireOptionalPattern(String value, String field, Pattern pattern) {
        if (value != null && !pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(field + " has an invalid format");
        }
    }
}
