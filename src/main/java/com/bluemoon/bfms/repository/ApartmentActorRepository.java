package com.bluemoon.bfms.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Resolves authenticated audit actors for BE-02, US-001 and NFR-02. */
@Repository
public class ApartmentActorRepository {
    private final JdbcTemplate jdbcTemplate;

    public ApartmentActorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> findActiveEditorId(String username) {
        return jdbcTemplate.query("""
                SELECT id FROM users
                WHERE username = ? AND is_active = TRUE
                  AND role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT')
                """, (row, index) -> row.getLong("id"), username).stream().findFirst();
    }
}
