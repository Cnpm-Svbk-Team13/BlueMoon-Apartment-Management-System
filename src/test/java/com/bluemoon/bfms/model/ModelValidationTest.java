package com.bluemoon.bfms.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Kiểm tra các bất biến ngày tháng được khai báo trực tiếp trên entity.
 */
class ModelValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void requiresDateRangeForTemporaryResidenceAndAcceptsValidRange() {
        Person person = new Person("Nguyễn An", "000000000001", "0900000001", null);
        Apartment apartment = new Apartment("A-601", 6, new BigDecimal("75.50"), ApartmentType.RESIDENTIAL);
        Household household = new Household(
                apartment, person, "HK-601", LocalDate.of(2026, 1, 1));

        Resident invalidResident = new Resident(person, household, RelationshipWithHead.TENANT,
                ResidenceStatus.TAM_TRU, null, null, null);
        Resident validResident = new Resident(person, household, RelationshipWithHead.TENANT,
                ResidenceStatus.TAM_TRU, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), null);

        assertFalse(validator.validate(invalidResident).isEmpty());
        assertTrue(validator.validate(validResident).isEmpty());
    }

    @Test
    void rejectsHouseholdEndDateBeforeMoveInDate() {
        Person person = new Person("Nguyễn An", "000000000001", "0900000001", null);
        Apartment apartment = new Apartment("A-601", 6, new BigDecimal("75.50"), ApartmentType.RESIDENTIAL);
        Household household = new Household(
                apartment, person, "HK-601", LocalDate.of(2026, 1, 1));

        household.close(HouseholdStatus.MOVED_OUT, LocalDate.of(2025, 12, 31));
        assertFalse(validator.validate(household).isEmpty());

        household.close(HouseholdStatus.MOVED_OUT, LocalDate.of(2026, 1, 1));
        assertTrue(validator.validate(household).isEmpty());
    }
}
