package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.HeadProfileForm;
import com.bluemoon.bfms.dto.HouseholdHeadForm;
import com.bluemoon.bfms.exception.ApartmentNotFoundException;
import com.bluemoon.bfms.model.*;
import com.bluemoon.bfms.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/** Implements atomic head assignment, replacement and profile updates for BE-03, US-001, RAW-002. */
@Service
public class HouseholdHeadService {
    private final ApartmentRepository apartments;
    private final PersonRepository persons;
    private final HouseholdRepository households;
    private final ResidentRepository residents;
    private final HouseholdHeadHistoryRepository history;
    private final AuditLogRepository audits;
    private final HouseholdWriteLock writeLock;
    private final Validator validator;

    public HouseholdHeadService(ApartmentRepository apartments, PersonRepository persons,
            HouseholdRepository households, ResidentRepository residents,
            HouseholdHeadHistoryRepository history, AuditLogRepository audits,
            HouseholdWriteLock writeLock, Validator validator) {
        this.apartments = apartments;
        this.persons = persons;
        this.households = households;
        this.residents = residents;
        this.history = history;
        this.audits = audits;
        this.writeLock = writeLock;
        this.validator = validator;
    }

    /**
     * Assigns or replaces the head of an apartment's active household.
     * @param apartmentId apartment ID
     * @param form validated head details and expected current head
     * @param actorId authenticated database user ID
     * @param ipAddress optional client IP
     * @throws ApartmentNotFoundException if the apartment does not exist
     * @throws IllegalArgumentException if input or current household state is invalid
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void assignOrReplace(Long apartmentId, HouseholdHeadForm form, Long actorId, String ipAddress) {
        validate(form, actorId, ipAddress);
        writeLock.acquire();
        Apartment apartment = apartments.findById(apartmentId).orElseThrow(ApartmentNotFoundException::new);
        Household household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE)
                .orElse(null);
        Person previous = household == null ? null : household.getHeadPerson();
        checkExpectedHead(form, previous);
        if (household == null && form.getMovedInDate() == null) {
            throw new IllegalArgumentException("Please enter the move-in date.");
        }

        Person next = resolveHead(form);
        if (previous != null && Objects.equals(previous.getId(), next.getId())) {
            throw new IllegalArgumentException("This person is already the household head.");
        }
        Resident nextResidence = residents.findByPersonId(next.getId()).orElse(null);
        if (nextResidence != null && (household == null
                || !Objects.equals(nextResidence.getHousehold().getId(), household.getId()))) {
            throw new IllegalArgumentException("The selected person resides in another household.");
        }

        if (household == null) {
            household = households.save(new Household(apartment, next,
                    UUID.randomUUID().toString().toUpperCase(Locale.ROOT), form.getMovedInDate()));
        } else {
            updateRelationships(household, previous, nextResidence, form.getPreviousHeadRelationship());
            household.changeHead(next);
        }
        recordChange(household, previous, next, previous == null ? null : snapshot(previous),
                actorId, ipAddress, previous == null ? "ASSIGN_HEAD" : "REPLACE_HEAD");
    }

    /**
     * Updates the shared head profile and records snapshots for every affected household.
     * @param apartmentId apartment ID
     * @param form validated profile and expected current head
     * @param actorId authenticated database user ID
     * @param ipAddress optional client IP
     * @throws IllegalArgumentException if input or current household state is invalid
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateProfile(Long apartmentId, HouseholdHeadForm form, Long actorId, String ipAddress) {
        validate(form, actorId, ipAddress);
        writeLock.acquire();
        Household household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("No active household exists."));
        Person person = household.getHeadPerson();
        checkExpectedHead(form, person);
        HeadProfileForm profile = form.getHeadPerson();
        persons.findByNationalId(profile.getNationalId()).ifPresent(existing -> {
            if (!Objects.equals(existing.getId(), person.getId())) {
                throw new IllegalArgumentException("National ID already belongs to another person.");
            }
        });
        JsonNode before = snapshot(person);
        applyProfile(person, profile);
        if (before.equals(snapshot(person))) {
            return;
        }
        for (Household affected : households.findAllByHeadPersonIdOrderById(person.getId())) {
            recordChange(affected, person, person, before, actorId, ipAddress, "UPDATE_HEAD_PROFILE");
        }
    }

    private void validate(HouseholdHeadForm form, Long actorId, String ipAddress) {
        if (form == null || !validator.validate(form).isEmpty()) {
            throw new IllegalArgumentException("Please provide valid head profile details.");
        }
        if (actorId == null || actorId <= 0 || (ipAddress != null && ipAddress.length() > 50)) {
            throw new IllegalArgumentException("Invalid audit actor or client IP address.");
        }
    }

    private void checkExpectedHead(HouseholdHeadForm form, Person currentHead) {
        Long currentId = currentHead == null ? null : currentHead.getId();
        if (!Objects.equals(form.getExpectedHeadPersonId(), currentId)) {
            throw new IllegalArgumentException("The household head has changed. Reload the page and try again.");
        }
    }

    private Person resolveHead(HouseholdHeadForm form) {
        HeadProfileForm profile = form.getHeadPerson();
        Person existing = persons.findByNationalId(profile.getNationalId()).orElse(null);
        if (existing != null) {
            if (!Objects.equals(existing.getId(), form.getConfirmedPersonId())) {
                throw new IllegalArgumentException("Look up and confirm the existing person before saving.");
            }
            if (existing.getPhone() == null) {
                throw new IllegalArgumentException("The existing person needs a valid phone number.");
            }
            return existing;
        }
        if (form.getConfirmedPersonId() != null) {
            throw new IllegalArgumentException("The selected person no longer matches the national ID.");
        }
        return persons.save(new Person(profile.getFullName().strip(), profile.getNationalId(),
                profile.getPhone(), normalizedEmail(profile.getEmail())));
    }

    private void updateRelationships(
            Household household, Person previous, Resident next, RelationshipWithHead replacement) {
        residents.findByPersonId(previous.getId())
                .filter(old -> Objects.equals(old.getHousehold().getId(), household.getId()))
                .ifPresent(old -> {
                    if (replacement == null || replacement == RelationshipWithHead.HEAD) {
                        throw new IllegalArgumentException("Select a new relationship for the previous head.");
                    }
                    old.changeRelationship(replacement);
                });
        if (next != null) {
            next.changeRelationship(RelationshipWithHead.HEAD);
        }
    }

    private void applyProfile(Person person, HeadProfileForm profile) {
        person.updateHeadProfile(profile.getFullName().strip(), profile.getNationalId(),
                profile.getPhone(), normalizedEmail(profile.getEmail()));
    }

    private String normalizedEmail(String email) {
        return email == null || email.isBlank() ? null : email.strip().toLowerCase(Locale.ROOT);
    }

    private JsonNode snapshot(Person person) {
        return JsonNodeFactory.instance.objectNode()
                .put("full_name", person.getFullName())
                .put("national_id", person.getNationalId())
                .put("phone", person.getPhone())
                .put("email", person.getEmail());
    }

    private void recordChange(Household household, Person previous, Person next, JsonNode before,
            Long actorId, String ipAddress, String action) {
        history.save(new HouseholdHeadHistory(household, previous, next, before, snapshot(next), actorId));
        audits.save(new AuditLog(actorId, action, "Household", household.getId(), null, ipAddress));
    }
}
