package com.bluemoon.bfms.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acquires the schema's shared identity write lock for BE-03. */
@Repository
public class HouseholdWriteLock {
    private final JdbcTemplate jdbcTemplate;

    public HouseholdWriteLock(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void acquire() {
        jdbcTemplate.execute("SELECT pg_advisory_xact_lock(3180, 13)");
    }
}
