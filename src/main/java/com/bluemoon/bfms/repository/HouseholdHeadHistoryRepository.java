package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.HouseholdHeadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Supports Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
public interface HouseholdHeadHistoryRepository extends JpaRepository<HouseholdHeadHistory, Long> {
    List<HouseholdHeadHistory> findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(Long apartmentId);
}
