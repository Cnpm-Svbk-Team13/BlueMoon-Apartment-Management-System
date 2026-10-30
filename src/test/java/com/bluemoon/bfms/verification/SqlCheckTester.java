package com.bluemoon.bfms.verification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class SqlCheckTester {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create base table
            stmt.execute("CREATE TABLE residents (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "residence_status VARCHAR(20) DEFAULT 'THUONG_TRU' CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG'))" +
                    ")");

            // Seed existing THUONG_TRU
            stmt.execute("INSERT INTO residents (full_name, residence_status) VALUES ('Nguyen Van An', 'THUONG_TRU')");

            // Apply proposed migration
            stmt.execute("ALTER TABLE residents ADD COLUMN start_date DATE");
            stmt.execute("ALTER TABLE residents ADD COLUMN end_date DATE");

            stmt.execute("ALTER TABLE residents ADD CONSTRAINT chk_residence_dates " +
                    "CHECK (residence_status = 'THUONG_TRU' OR " +
                    "(start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date))");

            System.out.println("Existing THUONG_TRU survived ALTER TABLE!");

            // Test 1: TAM_TRU with valid dates -> Should succeed
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Tran Van Tam', 'TAM_TRU', '2026-01-01', '2026-12-31')");
                System.out.println("Test 1 PASS: TAM_TRU with valid dates inserted.");
            } catch (Exception e) {
                System.out.println("Test 1 FAIL: " + e.getMessage());
            }

            // Test 2: TAM_TRU with end_date < start_date -> Should fail
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Tran Van Loi', 'TAM_TRU', '2026-12-31', '2026-01-01')");
                System.out.println("Test 2 FAIL: Invalid date order was allowed!");
            } catch (Exception e) {
                System.out.println("Test 2 PASS: Invalid date order blocked: " + e.getMessage());
            }

            // Test 3: TAM_TRU with NULL start_date -> Should fail
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Tran Van Loi', 'TAM_TRU', NULL, '2026-12-31')");
                System.out.println("Test 3 FAIL: NULL start_date for TAM_TRU was allowed!");
            } catch (Exception e) {
                System.out.println("Test 3 PASS: NULL start_date for TAM_TRU blocked: " + e.getMessage());
            }

            // Test 4: NULL residence_status -> What happens?!
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Null Status', NULL, NULL, NULL)");
                System.out.println("Test 4 VULNERABILITY CONFIRMED: NULL residence_status bypasses check constraints!");
            } catch (Exception e) {
                System.out.println("Test 4 PASS: NULL residence_status blocked: " + e.getMessage());
            }
        }
    }
}
