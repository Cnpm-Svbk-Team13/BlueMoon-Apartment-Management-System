package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides audit record persistence for BE-02, US-001, RAW-001 and RAW-002.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
