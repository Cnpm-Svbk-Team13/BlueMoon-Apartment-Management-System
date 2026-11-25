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

    public Optional<Long> findActiveEditorId(Long userId) {
        return jdbcTemplate.query("""
                SELECT id FROM users
                WHERE id = ? AND is_active = TRUE
                  AND role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT')
                """, (row, index) -> row.getLong("id"), userId).stream().findFirst();
    }

    /** Tìm ID tài khoản Admin đang hoạt động cho route quản trị tài khoản. */
    public Optional<Long> findActiveAdminId(Long userId) {
        return jdbcTemplate.query("""
                SELECT id FROM users
                WHERE id = ? AND is_active = TRUE
                  AND role = 'ROLE_ADMIN'
                """, (row, index) -> row.getLong("id"), userId).stream().findFirst();
    }

    /** Tìm ID tài khoản đang hoạt động không phụ thuộc role. */
    public Optional<Long> findActiveAccountId(Long userId) {
        return jdbcTemplate.query("""
                SELECT id FROM users
                WHERE id = ? AND is_active = TRUE
                """, (row, index) -> row.getLong("id"), userId).stream().findFirst();
    }
}
