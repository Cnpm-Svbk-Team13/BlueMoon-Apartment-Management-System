package com.bluemoon.bfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * Maps audit records for Sprint 1 task BE-02 (US-001, RAW-001 and RAW-002).
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "entity_name", nullable = false, length = 50)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "details", columnDefinition = "text")
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected AuditLog() {
    }

    /**
     * Creates an audit record; the database supplies its ID and creation time.
     *
     * @param userId acting user ID, or null when the actor is unknown
     * @param action recorded operation
     * @param entityName affected entity name
     * @param entityId affected entity ID, or null when unavailable
     * @param details optional operation details
     * @param ipAddress optional client IP address
     */
    public AuditLog(
            Long userId,
            String action,
            String entityName,
            Long entityId,
            String details,
            String ipAddress) {

        this.userId = userId;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.ipAddress = ipAddress;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
