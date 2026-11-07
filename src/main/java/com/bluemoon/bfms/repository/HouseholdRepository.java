package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bluemoon.bfms.model.HouseholdStatus;
import java.util.List;
import java.util.Optional;

/** Supports Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Optional<Household> findByApartmentIdAndStatus(Long apartmentId, HouseholdStatus status);
    List<Household> findAllByHeadPersonIdOrderById(Long personId);
}
