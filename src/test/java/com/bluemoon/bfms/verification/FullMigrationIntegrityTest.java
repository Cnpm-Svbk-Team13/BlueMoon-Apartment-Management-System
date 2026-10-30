package com.bluemoon.bfms.verification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FullMigrationIntegrityTest {
    public static void main(String[] args) {
        String url = "jdbc:h2:mem:migration_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";

        System.out.println("=== FULL MIGRATION INTEGRITY TEST ===");

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Step 1: Base schema creation (simulating schema.sql)
            stmt.execute("CREATE TABLE users (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE, " +
                    "phone VARCHAR(20), " +
                    "role VARCHAR(20) NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_CASHIER', 'ROLE_RESIDENT')), " +
                    "failed_attempts INT DEFAULT 0, " +
                    "locked_until TIMESTAMP WITH TIME ZONE, " +
                    "is_active BOOLEAN DEFAULT TRUE" +
                    ")");

            stmt.execute("CREATE TABLE apartments (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "room_number VARCHAR(20) UNIQUE NOT NULL, " +
                    "floor_number INT NOT NULL CHECK (floor_number BETWEEN 1 AND 30), " +
                    "area_sqm NUMERIC(8, 2) NOT NULL CHECK (area_sqm > 0), " +
                    "apartment_type VARCHAR(20) NOT NULL CHECK (apartment_type IN ('KIOT', 'RESIDENTIAL', 'PENTHOUSE', 'COMMERCIAL')), " +
                    "status VARCHAR(20) DEFAULT 'OCCUPIED' CHECK (status IN ('VACANT', 'OCCUPIED', 'RENOVATING'))" +
                    ")");

            stmt.execute("CREATE TABLE households (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "apartment_id BIGINT UNIQUE REFERENCES apartments(id) ON DELETE RESTRICT, " +
                    "registration_code VARCHAR(50) UNIQUE NOT NULL, " +
                    "head_name VARCHAR(100) NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'MOVED_OUT', 'TRANSFERRED'))" +
                    ")");

            stmt.execute("CREATE TABLE residents (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "household_id BIGINT REFERENCES households(id) ON DELETE CASCADE, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "date_of_birth DATE NOT NULL, " +
                    "gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')), " +
                    "national_id VARCHAR(20) UNIQUE, " +
                    "phone VARCHAR(20), " +
                    "relationship_with_head VARCHAR(50) NOT NULL, " +
                    "residence_status VARCHAR(20) DEFAULT 'THUONG_TRU' CHECK (residence_status IN ('THUONG_TRU', 'TAM_TRU', 'TAM_VANG'))" +
                    ")");

            stmt.execute("CREATE TABLE fee_types (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "fee_code VARCHAR(30) UNIQUE NOT NULL, " +
                    "fee_name VARCHAR(100) NOT NULL, " +
                    "category VARCHAR(30) NOT NULL CHECK (category IN ('SERVICE_MANDATORY', 'MANAGEMENT_MANDATORY', 'VOLUNTARY_FUND')), " +
                    "unit_price NUMERIC(12, 2) NOT NULL DEFAULT 0, " +
                    "calculation_unit VARCHAR(30) NOT NULL DEFAULT 'M2_MONTH', " +
                    "is_mandatory BOOLEAN NOT NULL DEFAULT TRUE, " +
                    "is_active BOOLEAN NOT NULL DEFAULT TRUE" +
                    ")");

            stmt.execute("CREATE TABLE campaign_funds (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "fund_name VARCHAR(150) NOT NULL, " +
                    "start_date DATE NOT NULL, " +
                    "end_date DATE, " +
                    "target_amount NUMERIC(15, 2) DEFAULT 0, " +
                    "current_amount NUMERIC(15, 2) DEFAULT 0, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CLOSED', 'PLANNED'))" +
                    ")");

            stmt.execute("CREATE TABLE invoices (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "invoice_code VARCHAR(50) UNIQUE NOT NULL, " +
                    "apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT, " +
                    "period_month INT NOT NULL CHECK (period_month BETWEEN 1 AND 12), " +
                    "period_year INT NOT NULL CHECK (period_year >= 2020), " +
                    "total_amount NUMERIC(15, 2) NOT NULL DEFAULT 0, " +
                    "payment_status VARCHAR(20) DEFAULT 'UNPAID' CHECK (payment_status IN ('UNPAID', 'PAID', 'OVERDUE', 'CANCELLED')), " +
                    "due_date DATE NOT NULL, " +
                    "CONSTRAINT uq_invoice_period UNIQUE (apartment_id, period_month, period_year)" +
                    ")");

            stmt.execute("CREATE TABLE invoice_items (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE, " +
                    "fee_type_id BIGINT NOT NULL REFERENCES fee_types(id) ON DELETE RESTRICT, " +
                    "item_name VARCHAR(100) NOT NULL, " +
                    "quantity NUMERIC(10, 2) NOT NULL, " +
                    "unit_price NUMERIC(12, 2) NOT NULL, " +
                    "amount NUMERIC(15, 2) NOT NULL" +
                    ")");

            stmt.execute("CREATE TABLE fund_contributions (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "fund_id BIGINT NOT NULL REFERENCES campaign_funds(id) ON DELETE RESTRICT, " +
                    "apartment_id BIGINT NOT NULL REFERENCES apartments(id) ON DELETE RESTRICT, " +
                    "contributor_name VARCHAR(100) NOT NULL, " +
                    "amount NUMERIC(15, 2) NOT NULL CHECK (amount > 0)" +
                    ")");

            stmt.execute("CREATE TABLE receipts (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "receipt_code VARCHAR(50) UNIQUE NOT NULL, " +
                    "invoice_id BIGINT REFERENCES invoices(id) ON DELETE SET NULL, " +
                    "contribution_id BIGINT REFERENCES fund_contributions(id) ON DELETE SET NULL, " +
                    "payer_name VARCHAR(100) NOT NULL, " +
                    "total_amount NUMERIC(15, 2) NOT NULL" +
                    ")");

            System.out.println("Step 1: Base schema created successfully.");

            // Step 2: Seed existing data
            stmt.execute("INSERT INTO apartments (room_number, floor_number, area_sqm, apartment_type) VALUES ('P.601', 6, 75.50, 'RESIDENTIAL')");
            stmt.execute("INSERT INTO households (apartment_id, registration_code, head_name) VALUES (1, 'HK-001', 'Nguyen Van An')");
            stmt.execute("INSERT INTO residents (household_id, full_name, date_of_birth, relationship_with_head, residence_status) " +
                    "VALUES (1, 'Nguyen Van An', '1980-01-01', 'CHU HO', 'THUONG_TRU')");
            stmt.execute("INSERT INTO invoices (invoice_code, apartment_id, period_month, period_year, total_amount, due_date) " +
                    "VALUES ('INV-001', 1, 10, 2026, 906000.00, '2026-10-31')");

            System.out.println("Step 2: Seed data inserted successfully.");

            // Step 3: Run the proposed DDL migration statements
            stmt.execute("ALTER TABLE residents ADD COLUMN start_date DATE");
            stmt.execute("ALTER TABLE residents ADD COLUMN end_date DATE");
            stmt.execute("ALTER TABLE residents ADD CONSTRAINT chk_residence_dates " +
                    "CHECK (residence_status = 'THUONG_TRU' OR " +
                    "(start_date IS NOT NULL AND end_date IS NOT NULL AND end_date >= start_date))");
            stmt.execute("ALTER TABLE residents ADD CONSTRAINT chk_national_id_format " +
                    "CHECK (national_id IS NULL OR national_id ~ '^[0-9]{12}$')");
            // Fix loophole: make residence_status NOT NULL
            stmt.execute("ALTER TABLE residents ALTER COLUMN residence_status SET NOT NULL");

            stmt.execute("ALTER TABLE invoices ADD COLUMN version BIGINT NOT NULL DEFAULT 0");

            stmt.execute("ALTER TABLE receipts ADD CONSTRAINT chk_receipt_exclusive_target " +
                    "CHECK ((invoice_id IS NOT NULL AND contribution_id IS NULL) OR " +
                    "(invoice_id IS NULL AND contribution_id IS NOT NULL))");

            stmt.execute("ALTER TABLE apartments ALTER COLUMN area_sqm TYPE DECIMAL(12,2)");

            System.out.println("Step 3: Migration statements executed cleanly with zero errors!");

        } catch (Exception e) {
            System.out.println("Migration FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
