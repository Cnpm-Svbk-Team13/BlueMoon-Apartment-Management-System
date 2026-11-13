package com.bluemoon.bfms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Ánh xạ một hộ gia đình hiện tại hoặc đã kết thúc trong bảng {@code households}.
 *
 * <p>Hộ tham chiếu căn hộ cố định và một hồ sơ chủ hộ. Khi hộ chuyển đi,
 * trạng thái/ngày kết thúc được lưu trên cùng bản ghi; chuyển sang căn khác
 * cần tạo hộ mới theo quy tắc dữ liệu, không đổi khóa ngoại căn của hộ cũ.</p>
 */
@Entity
@Table(name = "households")
public class Household {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Hộ gia đình phải thuộc một căn hộ.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false, updatable = false)
    private Apartment apartment;

    @NotNull(message = "Hộ gia đình phải có chủ hộ.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "head_person_id", nullable = false)
    private Person headPerson;

    @NotBlank(message = "Mã hộ không được để trống.")
    @Size(max = 50, message = "Mã hộ không được dài quá 50 ký tự.")
    @Column(name = "registration_code", nullable = false, length = 50)
    private String registrationCode;

    @NotNull(message = "Ngày chuyển vào không được để trống.")
    @Column(name = "moved_in_date", nullable = false)
    private LocalDate movedInDate;

    @Column(name = "moved_out_date")
    private LocalDate movedOutDate;

    @NotNull(message = "Trạng thái hộ không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HouseholdStatus status = HouseholdStatus.ACTIVE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Household() {
    }

    /**
     * Tạo hộ mới đang hoạt động; căn hộ sẽ không thể đổi sau khi lưu.
     *
     * @param apartment căn hộ cố định của hộ
     * @param headPerson hồ sơ người đại diện hộ
     * @param registrationCode mã hộ duy nhất
     * @param movedInDate ngày bắt đầu cư trú
     */
    public Household(Apartment apartment, Person headPerson, String registrationCode, LocalDate movedInDate) {
        this.apartment = apartment;
        this.headPerson = headPerson;
        this.registrationCode = registrationCode;
        this.movedInDate = movedInDate;
    }

    /**
     * Thay chủ hộ trong cùng hộ, không thay căn hộ được tham chiếu.
     *
     * @param headPerson hồ sơ chủ hộ mới
     */
    public void changeHead(Person headPerson) {
        this.headPerson = headPerson;
    }

    /**
     * Đóng hộ hoặc đánh dấu chuyển hộ; ngày kết thúc có thể null nếu chưa biết.
     *
     * @param status trạng thái kết thúc, chỉ nhận MOVED_OUT hoặc TRANSFERRED
     * @param movedOutDate ngày kết thúc đã biết, hoặc null nếu chưa xác định
     */
    public void close(HouseholdStatus status, LocalDate movedOutDate) {
        if (status == null || status == HouseholdStatus.ACTIVE) {
            throw new IllegalArgumentException("Hộ đã đóng phải có trạng thái MOVED_OUT hoặc TRANSFERRED.");
        }
        this.status = status;
        this.movedOutDate = movedOutDate;
    }

    /**
     * Kiểm tra nhất quán giữa trạng thái và ngày kết thúc trước khi lưu.
     * Quy tắc này tương đương CHECK constraint của schema PostgreSQL.
     *
     * @return true nếu ngày kết thúc phù hợp với trạng thái và ngày chuyển vào
     */
    @AssertTrue(message = "Ngày kết thúc hộ không phù hợp với trạng thái hoặc ngày chuyển vào.")
    private boolean isMoveOutDateValid() {
        if (status == null || movedInDate == null) {
            return true;
        }
        if (status == HouseholdStatus.ACTIVE) {
            return movedOutDate == null;
        }
        return movedOutDate == null || !movedOutDate.isBefore(movedInDate);
    }

    /** @return khóa chính của hộ */
    public Long getId() {
        return id;
    }

    /** @return căn hộ cố định của hộ */
    public Apartment getApartment() {
        return apartment;
    }

    /** @return hồ sơ chủ hộ hiện tại */
    public Person getHeadPerson() {
        return headPerson;
    }

    /** @return mã hộ */
    public String getRegistrationCode() {
        return registrationCode;
    }

    /** @return ngày chuyển vào */
    public LocalDate getMovedInDate() {
        return movedInDate;
    }

    /** @return ngày chuyển ra hoặc {@code null} nếu chưa xác định/chưa kết thúc */
    public LocalDate getMovedOutDate() {
        return movedOutDate;
    }

    /** @return trạng thái hiện tại của hộ */
    public HouseholdStatus getStatus() {
        return status;
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
