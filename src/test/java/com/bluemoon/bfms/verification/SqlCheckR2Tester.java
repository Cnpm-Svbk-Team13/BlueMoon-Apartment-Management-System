package com.bluemoon.bfms.verification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlCheckR2Tester {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create base table as in schema.sql
            stmt.execute("CREATE TABLE residents (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "residence_status VARCHAR(20) DEFAULT 'THUONG_TRU' CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG'))" +
                    ")");

            // Seed existing THUONG_TRU
            stmt.execute("INSERT INTO residents (full_name, residence_status) VALUES ('Nguyen Van An', 'THUONG_TRU')");

            // Apply remediated migration V1.1
            stmt.execute("ALTER TABLE residents ALTER COLUMN residence_status SET NOT NULL");
            stmt.execute("ALTER TABLE residents ADD COLUMN IF NOT EXISTS start_date DATE");
            stmt.execute("ALTER TABLE residents ADD COLUMN IF NOT EXISTS end_date DATE");

            stmt.execute("ALTER TABLE residents DROP CONSTRAINT IF EXISTS chk_residence_dates");
            stmt.execute("ALTER TABLE residents ADD CONSTRAINT chk_residence_dates " +
                    "CHECK (residence_status = 'THUONG_TRU' OR " +
                    "(start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date))");

            System.out.println("Remediated DDL applied successfully!");

            // Test 1: TAM_TRU with valid dates -> Should succeed
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Tran Van Tam', 'TAM_TRU', '2026-01-01', '2026-12-31')");
                System.out.println("Test 1 PASS: TAM_TRU with valid dates accepted.");
            } catch (Exception e) {
                System.out.println("Test 1 FAIL: " + e.getMessage());
            }

            // Test 2: TAM_TRU with end_date < start_date -> Should fail
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Tran Van Loi', 'TAM_TRU', '2026-12-31', '2026-01-01')");
                System.out.println("Test 2 FAIL: Invalid date order allowed!");
            } catch (Exception e) {
                System.out.println("Test 2 PASS: Invalid date order blocked: " + e.getMessage());
            }

            // Test 3: NULL residence_status -> Should be BLOCKED by NOT NULL!
            try {
                stmt.execute("INSERT INTO residents (full_name, residence_status, start_date, end_date) " +
                        "VALUES ('Null Status', NULL, '2026-01-01', '2026-12-31')");
                System.out.println("Test 3 FAIL: NULL residence_status was allowed!");
            } catch (Exception e) {
                System.out.println("Test 3 PASS: NULL residence_status blocked by NOT NULL constraint: " + e.getMessage());
            }

            // Test 4: Default value on omit residence_status -> Should be THUONG_TRU and succeed
            try {
                stmt.execute("INSERT INTO residents (full_name) VALUES ('Default Thuong Tru')");
                System.out.println("Test 4 PASS: Default THUONG_TRU works.");
            } catch (Exception e) {
                System.out.println("Test 4 FAIL: " + e.getMessage());
            }
        }
    }
}
