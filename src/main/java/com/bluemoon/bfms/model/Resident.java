package com.bluemoon.bfms.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.LocalDate;

/** Maps residents for Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
@Entity
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
    private ResidenceStatus residenceStatus;

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

    public Household getHousehold() {
        return household;
    }

    public void changeRelationship(RelationshipWithHead relationship) {
        this.relationshipWithHead = relationship;
    }
}
