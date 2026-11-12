package com.bluemoon.bfms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Ánh xạ hồ sơ cư trú hiện tại trong bảng {@code residents}.
 *
 * <p>Thông tin định danh được đọc qua {@link Person}; bảng này chỉ giữ liên kết
 * hộ gia đình, quan hệ với chủ hộ, trạng thái cư trú và khoảng ngày. Một người
 * chỉ có tối đa một hồ sơ cư trú hiện tại theo UNIQUE constraint của schema.</p>
 */
@Entity
@Table(name = "residents")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Hồ sơ cư trú phải tham chiếu một người.")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @NotNull(message = "Hồ sơ cư trú phải thuộc một hộ.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @NotNull(message = "Quan hệ với chủ hộ không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_with_head", nullable = false, length = 50)
    private RelationshipWithHead relationshipWithHead;

    @NotNull(message = "Trạng thái cư trú không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "residence_status", nullable = false, length = 20)
    private ResidenceStatus residenceStatus = ResidenceStatus.THUONG_TRU;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Resident() {
    }

    /**
     * Tạo hồ sơ cư trú; nếu là tạm trú/tạm vắng, hai ngày phải được cung cấp.
     *
     * @param person người có hồ sơ cư trú
     * @param household hộ mà người đó đang cư trú
     * @param relationshipWithHead quan hệ chuẩn hóa với chủ hộ
     * @param residenceStatus trạng thái cư trú
     * @param startDate ngày bắt đầu tạm trú/tạm vắng, hoặc null với thường trú
     * @param endDate ngày kết thúc tạm trú/tạm vắng, hoặc null với thường trú
     * @param notes ghi chú tùy chọn
     */
    public Resident(Person person, Household household, RelationshipWithHead relationshipWithHead,
            ResidenceStatus residenceStatus, LocalDate startDate, LocalDate endDate, String notes) {
        this.person = person;
        this.household = household;
        this.relationshipWithHead = relationshipWithHead;
        this.residenceStatus = residenceStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    /**
     * Bảo đảm thường trú không mang khoảng ngày, còn tạm trú/tạm vắng có đủ
     * hai ngày hợp lệ. Quy tắc tương ứng CHECK constraint trong schema.
     *
     * @return true khi trạng thái và ngày bắt đầu/kết thúc nhất quán
     */
    @AssertTrue(message = "Ngày cư trú phải phù hợp với trạng thái đã chọn.")
    private boolean isResidenceDateRangeValid() {
        if (residenceStatus == null) {
            return true;
        }
        if (residenceStatus == ResidenceStatus.THUONG_TRU) {
            return startDate == null && endDate == null;
        }
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }

    /** @return khóa chính của hồ sơ cư trú */
    public Long getId() {
        return id;
    }

    /** @return hồ sơ định danh của người cư trú */
    public Person getPerson() {
        return person;
    }

    /** @return hộ gia đình mà người này đang cư trú */
    public Household getHousehold() {
        return household;
    }

    /** @return quan hệ chuẩn hóa với chủ hộ */
    public RelationshipWithHead getRelationshipWithHead() {
        return relationshipWithHead;
    }

    /** @return trạng thái cư trú */
    public ResidenceStatus getResidenceStatus() {
        return residenceStatus;
    }

    /** @return ngày bắt đầu nếu là tạm trú/tạm vắng */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** @return ngày kết thúc nếu là tạm trú/tạm vắng */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** @return ghi chú hoặc {@code null} */
    public String getNotes() {
        return notes;
    }

    /** @return thời điểm tạo do database ghi nhận */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /** @return thời điểm cập nhật gần nhất do database ghi nhận */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Cập nhật quan hệ với chủ hộ khi hộ thay đổi chủ.
     *
     * @param relationship quan hệ mới trong danh mục chuẩn
     */
    public void changeRelationship(RelationshipWithHead relationship) {
        this.relationshipWithHead = relationship;
    }

    /**
     * Cập nhật trạng thái và khoảng ngày cư trú trên cùng hồ sơ hiện tại.
     *
     * @param status trạng thái cư trú mới
     * @param startDate ngày bắt đầu, bắt buộc cho tạm trú/tạm vắng
     * @param endDate ngày kết thúc, bắt buộc cho tạm trú/tạm vắng
     */
    public void changeResidence(ResidenceStatus status, LocalDate startDate, LocalDate endDate) {
        this.residenceStatus = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
