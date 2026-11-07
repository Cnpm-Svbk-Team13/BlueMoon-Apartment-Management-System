package com.bluemoon.bfms.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** Maps household_head_history for Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
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

    @Column(name = "changed_by_user_id", nullable = false)
    private Long changedByUserId;

    protected HouseholdHeadHistory() {
    }

    public HouseholdHeadHistory(Household household, Person previousHeadPerson, Person newHeadPerson,
            JsonNode previousSnapshot, JsonNode newSnapshot, Long changedByUserId) {
        this.household = household;
        this.previousHeadPerson = previousHeadPerson;
        this.newHeadPerson = newHeadPerson;
        this.previousSnapshot = previousSnapshot;
        this.newSnapshot = newSnapshot;
        this.changedByUserId = changedByUserId;
    }

    public Long getId() {
        return id;
    }

    public Household getHousehold() {
        return household;
    }

    public Person getPreviousHeadPerson() {
        return previousHeadPerson;
    }

    public Person getNewHeadPerson() {
        return newHeadPerson;
    }

    public JsonNode getPreviousSnapshot() {
        return previousSnapshot;
    }

    public JsonNode getNewSnapshot() {
        return newSnapshot;
    }

    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    public Long getChangedByUserId() {
        return changedByUserId;
    }
}
