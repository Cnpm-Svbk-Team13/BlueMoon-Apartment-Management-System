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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/** JPA entity hồ sơ cư trú hiện tại, tham chiếu hồ sơ cá nhân và hộ gia đình. */
@Entity
@org.hibernate.annotations.DynamicInsert
@Table(name = "residents")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_with_head", nullable = false, length = 50)
    private RelationshipWithHead relationshipWithHead;

    @Enumerated(EnumType.STRING)
    @Column(name = "residence_status", nullable = false, length = 20)
    private ResidenceStatus residenceStatus = ResidenceStatus.THUONG_TRU;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.Generated(event = {
            org.hibernate.generator.EventType.INSERT,
            org.hibernate.generator.EventType.UPDATE
    })
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Resident() {
    }

    public Resident(
            Long id,
            Person person,
            Household household,
            RelationshipWithHead relationshipWithHead,
            ResidenceStatus residenceStatus,
            LocalDate startDate,
            LocalDate endDate,
            String notes,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.person = person;
        this.household = household;
        this.relationshipWithHead = relationshipWithHead;
        this.residenceStatus = residenceStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() { return id; }
    public Person person() { return person; }
    public Long personId() { return person == null ? null : person.id(); }
    public Household household() { return household; }
    public Long householdId() { return household == null ? null : household.id(); }
    public RelationshipWithHead relationshipWithHead() { return relationshipWithHead; }
    public ResidenceStatus residenceStatus() { return residenceStatus; }
    public LocalDate startDate() { return startDate; }
    public LocalDate endDate() { return endDate; }
    public String notes() { return notes; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    /**
     * Kiểm tra liên kết và khoảng thời gian cư trú của nhân khẩu.
     *
     * <p>ID cá nhân và hộ phải dương; quan hệ với chủ hộ và trạng thái cư trú
     * phải được khai báo. THUONG_TRU không được có ngày bắt đầu/kết
     * thúc; TAM_TRU và TAM_VANG phải có đủ cả hai ngày, trong
     * đó ngày kết thúc không được trước ngày bắt đầu. Phương thức không kiểm
     * tra số lượng nhân khẩu hay quan hệ nhất quán ở các bản ghi khác.</p>
     *
     * Có thể ném IllegalArgumentException nếu dữ liệu vi phạm ràng buộc cục bộ
     */
    public void validate() {
        if (person == null || person.id() == null || person.id() <= 0
                || household == null || household.id() == null || household.id() <= 0) {
            throw new IllegalArgumentException("personId and householdId must be positive");
        }
        if (relationshipWithHead == null || residenceStatus == null) {
            throw new IllegalArgumentException("relationshipWithHead and residenceStatus are required");
        }
        boolean hasAnyDate = startDate != null || endDate != null;
        boolean hasBothDates = startDate != null && endDate != null;
        if ((residenceStatus.requiresDateInterval() && !hasBothDates)
                || (!residenceStatus.requiresDateInterval() && hasAnyDate)) {
            throw new IllegalArgumentException(
                    "Temporary and absent residence statuses require both dates; permanent residence does not");
        }
        if (startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot precede startDate");
        }
    }

    /**
     * Kiểm tra quan hệ của nhân khẩu có được khai báo là chủ hộ hay không.
     *
     * <p>Đây chỉ là phép kiểm tra giá trị enum, không xác minh rằng ID cá nhân
     * này trùng với chủ hộ được khai báo trong hồ sơ hộ.</p>
     *
     * Kết quả: true nếu quan hệ là RelationshipWithHead.HEAD
     */
    public boolean isHouseholdHead() {
        return relationshipWithHead == RelationshipWithHead.HEAD;
    }

    /**
     * Kiểm tra ngày chỉ định có nằm trong thời gian cư trú của hồ sơ hay không.
     *
     * <p>Thời gian tạm trú/tạm vắng được tính bao gồm cả ngày bắt đầu và ngày
     * kết thúc. Thường trú luôn được xem là bao phủ ngày đang xét. Trước khi
     * so sánh, phương thức xác thực khoảng ngày của đối tượng hiện tại.</p>
     *
     * Tham số date: ngày cần kiểm tra, không được null
     * Kết quả: true nếu ngày nằm trong khoảng áp dụng hoặc trạng thái là thường trú
     * Có thể ném IllegalArgumentException nếu ngày là null hoặc dữ liệu cư trú không hợp lệ
     */
    public boolean coversDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        validate();
        return !residenceStatus.requiresDateInterval()
                || (!date.isBefore(startDate) && !date.isAfter(endDate));
    }

    /**
     * Tạo bản sao với trạng thái cư trú và khoảng ngày mới.
     *
     * <p>Hàm tạo entity mới rồi gọi validate() trước khi trả về. Vì
     * vậy tạm trú/tạm vắng phải có đủ ngày đầu-cuối; thường trú phải để cả hai
     * ngày là null. Entity hiện tại không bị thay đổi.</p>
     *
     * Tham số newStatus: loại cư trú mới
     * Tham số newStartDate: ngày bắt đầu; phải có với trạng thái tạm trú/tạm vắng
     * Tham số newEndDate: ngày kết thúc; phải có và không trước ngày bắt đầu với trạng thái tạm trú/tạm vắng
     * Kết quả: bản sao nhân khẩu với thông tin cư trú mới
     * Có thể ném IllegalArgumentException nếu trạng thái hoặc khoảng ngày không hợp lệ
     */
    public Resident withResidence(
            ResidenceStatus newStatus,
            LocalDate newStartDate,
            LocalDate newEndDate) {
        Resident updated = new Resident(id, person, household, relationshipWithHead,
                newStatus, newStartDate, newEndDate, notes, createdAt, updatedAt);
        updated.validate();
        return updated;
    }
}
