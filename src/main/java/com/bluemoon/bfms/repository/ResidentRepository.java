package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Supports Sprint 1 BE-01/DB-02/BE-03 (US-001, RAW-002). */
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByPersonId(Long personId);
}
