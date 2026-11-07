package com.bluemoon.bfms.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.LocalDate;

/** Maps persons for Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
@Entity
@Table(name = "persons")
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

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Person() {
    }

    public Person(String fullName, String nationalId, String phone, String email) {
        updateHeadProfile(fullName, nationalId, phone, email);
    }

    public void updateHeadProfile(String fullName, String nationalId, String phone, String email) {
        this.fullName = fullName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
