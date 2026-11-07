package com.bluemoon.bfms.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.LocalDate;

/** Maps households for Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
@Entity
@Table(name = "households")
public class Household {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false, updatable = false)
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

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Household() {
    }

    public Household(Apartment apartment, Person headPerson, String registrationCode, LocalDate movedInDate) {
        this.apartment = apartment;
        this.headPerson = headPerson;
        this.registrationCode = registrationCode;
        this.movedInDate = movedInDate;
    }

    public void changeHead(Person headPerson) {
        this.headPerson = headPerson;
    }

    public Long getId() {
        return id;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public Person getHeadPerson() {
        return headPerson;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public LocalDate getMovedInDate() {
        return movedInDate;
    }
}
