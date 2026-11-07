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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;

/** JPA entity hộ gia đình, liên kết căn hộ và chủ hộ bằng quan hệ khóa ngoại. */
@Entity
@org.hibernate.annotations.DynamicInsert
@Table(name = "households", uniqueConstraints = {
        @UniqueConstraint(name = "households_registration_code_key", columnNames = "registration_code")
})
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "head_person_id", nullable = false)
    private Person headPerson;

    @Column(name = "registration_code", nullable = false, length = 50)
    private String registrationCode;

    @Column(name = "moved_in_date", nullable = false)
    private LocalDate movedInDate;

    @Column(name = "moved_out_date")
    private LocalDate movedOutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HouseholdStatus status = HouseholdStatus.ACTIVE;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.Generated(event = {
            org.hibernate.generator.EventType.INSERT,
            org.hibernate.generator.EventType.UPDATE
    })
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Household() {
    }

    public Household(
            Long id,
            Apartment apartment,
            Person headPerson,
            String registrationCode,
            LocalDate movedInDate,
            LocalDate movedOutDate,
            HouseholdStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.apartment = apartment;
        this.headPerson = headPerson;
        this.registrationCode = registrationCode;
        this.movedInDate = movedInDate;
        this.movedOutDate = movedOutDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() { return id; }
    public Apartment apartment() { return apartment; }
    public Long apartmentId() { return apartment == null ? null : apartment.id(); }
    public Person headPerson() { return headPerson; }
    public Long headPersonId() { return headPerson == null ? null : headPerson.id(); }
    public String registrationCode() { return registrationCode; }
    public LocalDate movedInDate() { return movedInDate; }
    public LocalDate movedOutDate() { return movedOutDate; }
    public HouseholdStatus status() { return status; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    /**
     * Kiểm tra các ràng buộc cục bộ của hộ gia đình.
     *
     * <p>ID căn hộ và chủ hộ phải dương; mã đăng ký phải viết hoa, đã trim
     * và tối đa 50 ký tự; ngày chuyển vào và trạng thái phải có giá trị. Hộ
     * đang hoạt động không được có ngày chuyển đi; nếu có ngày chuyển đi thì
     * ngày đó không được trước ngày chuyển vào. Tính duy nhất và việc chỉ có
     * một hộ hoạt động trên mỗi căn cần được bảo đảm ở tầng lưu trữ.</p>
     *
     * Có thể ném IllegalArgumentException nếu một ràng buộc cục bộ không được đáp ứng
     */
    public void validate() {
        if (apartment == null || apartment.id() == null || apartment.id() <= 0
                || headPerson == null || headPerson.id() == null || headPerson.id() <= 0) {
            throw new IllegalArgumentException("apartmentId and headPersonId must be positive");
        }
        if (registrationCode == null || registrationCode.isBlank()
                || registrationCode.length() > 50
                || !registrationCode.equals(registrationCode.trim().toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException(
                    "registrationCode must be trimmed, uppercase, and at most 50 characters");
        }
        if (movedInDate == null || status == null) {
            throw new IllegalArgumentException("movedInDate and status are required");
        }
        if (status == HouseholdStatus.ACTIVE && movedOutDate != null) {
            throw new IllegalArgumentException("An active household cannot have a moved-out date");
        }
        if (movedOutDate != null && movedOutDate.isBefore(movedInDate)) {
            throw new IllegalArgumentException("movedOutDate cannot precede movedInDate");
        }
    }

    /**
     * Kiểm tra hộ có đang ở trạng thái hoạt động hay không.
     *
     * Kết quả: true nếu trạng thái là HouseholdStatus.ACTIVE
     */
    public boolean isActive() {
        return status == HouseholdStatus.ACTIVE;
    }

    /**
     * Tạo bản sao ghi nhận hộ đã chuyển đi hoặc chuyển giao.
     *
     * <p>Chỉ hộ đang hoạt động mới được đóng. Trạng thái cuối phải là
     * HouseholdStatus.MOVED_OUT hoặc HouseholdStatus.TRANSFERRED;
     * ngày kết thúc không được trước ngày chuyển vào. Đối tượng hiện tại không
     * thay đổi; service chịu trách nhiệm lưu trạng thái mới và cập nhật trạng
     * thái căn hộ trong cùng giao dịch nếu cần.</p>
     *
     * Tham số finalStatus: trạng thái kết thúc hợp lệ
     * Tham số date: ngày chuyển đi/chuyển giao, không được null
     * Kết quả: bản sao hộ ở trạng thái kết thúc
     * Có thể ném IllegalStateException nếu hộ hiện tại không hoạt động
     * Có thể ném IllegalArgumentException nếu trạng thái hoặc ngày không hợp lệ
     */
    public Household close(HouseholdStatus finalStatus, LocalDate date) {
        if (!isActive()) {
            throw new IllegalStateException("Only an active household can be closed");
        }
        if (finalStatus != HouseholdStatus.MOVED_OUT && finalStatus != HouseholdStatus.TRANSFERRED) {
            throw new IllegalArgumentException("finalStatus must be MOVED_OUT or TRANSFERRED");
        }
        if (date == null || (movedInDate != null && date.isBefore(movedInDate))) {
            throw new IllegalArgumentException("date must not precede movedInDate");
        }
        return new Household(id, apartment, headPerson, registrationCode, movedInDate,
                date, finalStatus, createdAt, updatedAt);
    }

    /**
     * Tạo bản sao khôi phục hộ về trạng thái hoạt động.
     *
     * <p>Ngày chuyển đi được xóa. Phương thức từ chối nếu hộ vốn đã hoạt động.
     * Trước khi lưu, service phải kiểm tra quy tắc liên bản ghi: căn hộ không
     * được đồng thời có hộ hoạt động khác.</p>
     *
     * Kết quả: bản sao có trạng thái HouseholdStatus.ACTIVE và không có ngày chuyển đi
     * Có thể ném IllegalStateException nếu hộ đã ở trạng thái hoạt động
     */
    public Household reactivate() {
        if (isActive()) {
            throw new IllegalStateException("The household is already active");
        }
        return new Household(id, apartment, headPerson, registrationCode, movedInDate,
                null, HouseholdStatus.ACTIVE, createdAt, updatedAt);
    }

    /**
     * Tạo bản sao có mã đăng ký đã trim và viết hoa.
     *
     * <p>Phương thức chỉ chuẩn hóa; hãy gọi validate() để kiểm tra mã
     * không rỗng và đúng giới hạn độ dài trước khi lưu.</p>
     *
     * Kết quả: bản sao hộ có mã đăng ký chuẩn hóa; mã null vẫn giữ nguyên để việc kiểm tra báo lỗi tập trung
     */
    public Household normalized() {
        String normalizedCode = registrationCode == null
                ? null
                : registrationCode.trim().toUpperCase(Locale.ROOT);
        return new Household(id, apartment, headPerson, normalizedCode, movedInDate,
                movedOutDate, status, createdAt, updatedAt);
    }
}
