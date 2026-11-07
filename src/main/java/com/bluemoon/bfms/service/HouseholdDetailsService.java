package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.ApartmentHouseholdView;
import com.bluemoon.bfms.dto.HeadProfileForm;
import com.bluemoon.bfms.exception.ApartmentNotFoundException;
import com.bluemoon.bfms.model.HouseholdStatus;
import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.HouseholdHeadHistoryRepository;
import com.bluemoon.bfms.repository.HouseholdRepository;
import com.bluemoon.bfms.repository.PersonRepository;
import com.bluemoon.bfms.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Loads head profiles and history for BE-03, US-001 and RAW-002. */
@Service
public class HouseholdDetailsService {
    private final ApartmentRepository apartments;
    private final HouseholdRepository households;
    private final HouseholdHeadHistoryRepository history;
    private final ResidentRepository residents;
    private final PersonRepository persons;

    public HouseholdDetailsService(ApartmentRepository apartments, HouseholdRepository households,
            HouseholdHeadHistoryRepository history, ResidentRepository residents, PersonRepository persons) {
        this.apartments = apartments;
        this.households = households;
        this.history = history;
        this.residents = residents;
        this.persons = persons;
    }

    /**
     * Loads a detached read model including stored snapshots for the apartment.
     * @param apartmentId apartment ID
     * @return apartment details, active head if present and historical entries
     * @throws ApartmentNotFoundException if the apartment does not exist
     */
    @Transactional(readOnly = true)
    public ApartmentHouseholdView getDetails(Long apartmentId) {
        var apartment = apartments.findById(apartmentId).orElseThrow(ApartmentNotFoundException::new);
        var household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE).orElse(null);
        Person head = household == null ? null : household.getHeadPerson();
        boolean headResidesHere = head != null && residents.findByPersonId(head.getId())
                .filter(resident -> resident.getHousehold().getId().equals(household.getId())).isPresent();
        var entries = history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(apartmentId).stream()
                .map(entry -> {
                    String event = entry.getPreviousHeadPerson() == null ? "ASSIGNED"
                            : entry.getPreviousHeadPerson().getId().equals(entry.getNewHeadPerson().getId())
                                    ? "PROFILE_UPDATED" : "REPLACED";
                    return new ApartmentHouseholdView.HistoryEntry(event, entry.getPreviousSnapshot(),
                            entry.getNewSnapshot(), entry.getChangedAt(), entry.getChangedByUserId());
                }).toList();
        return new ApartmentHouseholdView(apartment.getId(), apartment.getRoomNumber(),
                head == null ? null : head.getId(), head == null ? null : toForm(head), headResidesHere, entries);
    }

    /**
     * Looks up the profile behind a national ID for an authorized editor.
     * @param nationalId national ID to find
     * @return matching profile and ID if a person exists
     * @throws IllegalArgumentException if the national ID is malformed
     */
    @Transactional(readOnly = true)
    public Optional<ExistingPerson> findExistingPerson(String nationalId) {
        if (nationalId == null || !nationalId.matches("[0-9]{12}")) {
            throw new IllegalArgumentException("National ID must contain 12 digits.");
        }
        return persons.findByNationalId(nationalId).map(person -> new ExistingPerson(person.getId(), toForm(person)));
    }

    private HeadProfileForm toForm(Person person) {
        HeadProfileForm form = new HeadProfileForm();
        form.setFullName(person.getFullName());
        form.setNationalId(person.getNationalId());
        form.setPhone(person.getPhone());
        form.setEmail(person.getEmail());
        return form;
    }

    public record ExistingPerson(Long id, HeadProfileForm profile) {
    }
}
