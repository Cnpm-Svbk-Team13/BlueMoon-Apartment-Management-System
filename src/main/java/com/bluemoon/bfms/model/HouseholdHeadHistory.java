package com.bluemoon.bfms.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Ánh xạ một lần gán/thay đổi hồ sơ chủ hộ trong
 * {@code household_head_history}.
 *
 * <p>Snapshot JSONB lưu dữ liệu tại thời điểm sự kiện, không phải dữ liệu hiện
 * tại của hồ sơ. Hộ, hai hồ sơ chủ hộ và tài khoản người thực hiện được ánh xạ
 * thành quan hệ JPA tương ứng với các khóa ngoại của schema.</p>
 */
@Entity
@Table(name = "household_head_history")
public class HouseholdHeadHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_head_person_id")
    private Person previousHeadPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "new_head_person_id", nullable = false)
    private Person newHeadPerson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_snapshot", columnDefinition = "jsonb")
    private JsonNode previousSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_snapshot", nullable = false, columnDefinition = "jsonb")
    private JsonNode newSnapshot;

    @Column(name = "changed_at", insertable = false, updatable = false)
    private OffsetDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private User changedByUser;

    protected HouseholdHeadHistory() {
    }

    /**
     * Tạo sự kiện lịch sử bất biến; thông tin snapshot phải được Service chụp
     * trước khi thay đổi dữ liệu và lưu cùng transaction với thao tác nghiệp vụ.
     *
     * @param household hộ liên quan tới sự kiện
     * @param previousHeadPerson chủ hộ trước đó, null khi gán lần đầu
     * @param newHeadPerson chủ hộ sau thay đổi
     * @param previousSnapshot snapshot trước đó, null khi gán lần đầu
     * @param newSnapshot snapshot mới sau thay đổi
     * @param changedByUser tài khoản thực hiện thao tác
     */
    public HouseholdHeadHistory(Household household, Person previousHeadPerson, Person newHeadPerson,
            JsonNode previousSnapshot, JsonNode newSnapshot, User changedByUser) {
        this.household = household;
        this.previousHeadPerson = previousHeadPerson;
        this.newHeadPerson = newHeadPerson;
        this.previousSnapshot = previousSnapshot;
        this.newSnapshot = newSnapshot;
        this.changedByUser = changedByUser;
    }

    /** @return khóa chính sự kiện lịch sử */
    public Long getId() {
        return id;
    }

    /** @return hộ phát sinh sự kiện */
    public Household getHousehold() {
        return household;
    }

    /** @return chủ hộ trước đó, null nếu là lần gán đầu */
    public Person getPreviousHeadPerson() {
        return previousHeadPerson;
    }

    /** @return chủ hộ sau sự kiện */
    public Person getNewHeadPerson() {
        return newHeadPerson;
    }

    /** @return snapshot hồ sơ trước thay đổi hoặc null khi gán đầu */
    public JsonNode getPreviousSnapshot() {
        return previousSnapshot;
    }

    /** @return snapshot hồ sơ sau thay đổi */
    public JsonNode getNewSnapshot() {
        return newSnapshot;
    }

    /**
     * Suy ra mã sự kiện từ cặp chủ hộ trước/sau thay vì lưu một cột event_type
     * trùng lặp trong database.
     *
     * @return ASSIGNED nếu gán lần đầu, PROFILE_UPDATED nếu cùng một người,
     *         hoặc REPLACED nếu đổi sang người khác
     */
    public String getEventType() {
        if (previousHeadPerson == null) {
            return "ASSIGNED";
        }
        boolean sameHead = previousHeadPerson == newHeadPerson
                || (previousHeadPerson.getId() != null
                        && previousHeadPerson.getId().equals(newHeadPerson.getId()));
        return sameHead
                ? "PROFILE_UPDATED"
                : "REPLACED";
    }

    /** @return thời điểm sự kiện do database ghi nhận */
    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    /** @return tài khoản thực hiện sự kiện */
    public User getChangedByUser() {
        return changedByUser;
    }

    /** @return ID tài khoản thực hiện sự kiện */
    public Long getChangedByUserId() {
        return changedByUser == null ? null : changedByUser.getId();
    }
}
