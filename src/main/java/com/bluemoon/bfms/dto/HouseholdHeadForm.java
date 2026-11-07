package com.bluemoon.bfms.dto;

import com.bluemoon.bfms.model.RelationshipWithHead;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/** Carries head assignment and profile changes for BE-03 and US-001. */
public class HouseholdHeadForm {
    @Valid
    @NotNull
    private HeadProfileForm headPerson = new HeadProfileForm();

    private Long confirmedPersonId;

    private Long expectedHeadPersonId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate movedInDate;

    private RelationshipWithHead previousHeadRelationship;

    public HeadProfileForm getHeadPerson() {
        return headPerson;
    }

    public void setHeadPerson(HeadProfileForm headPerson) {
        this.headPerson = headPerson;
    }

    public Long getConfirmedPersonId() {
        return confirmedPersonId;
    }

    public void setConfirmedPersonId(Long confirmedPersonId) {
        this.confirmedPersonId = confirmedPersonId;
    }

    public Long getExpectedHeadPersonId() {
        return expectedHeadPersonId;
    }

    public void setExpectedHeadPersonId(Long expectedHeadPersonId) {
        this.expectedHeadPersonId = expectedHeadPersonId;
    }

    public LocalDate getMovedInDate() {
        return movedInDate;
    }

    public void setMovedInDate(LocalDate movedInDate) {
        this.movedInDate = movedInDate;
    }

    public RelationshipWithHead getPreviousHeadRelationship() {
        return previousHeadRelationship;
    }

    public void setPreviousHeadRelationship(RelationshipWithHead previousHeadRelationship) {
        this.previousHeadRelationship = previousHeadRelationship;
    }
}
