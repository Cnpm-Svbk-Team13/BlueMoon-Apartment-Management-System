package com.bluemoon.bfms.verification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class XorConstraintTester {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:test_xor;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Create receipts table matching schema.sql
            stmt.execute("CREATE TABLE receipts (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "receipt_code VARCHAR(50) UNIQUE NOT NULL, " +
                    "invoice_id BIGINT, " +
                    "contribution_id BIGINT, " +
                    "payer_name VARCHAR(100) NOT NULL, " +
                    "total_amount NUMERIC(15, 2) NOT NULL" +
                    ")");

            // Apply proposed XOR constraint
            stmt.execute("ALTER TABLE receipts ADD CONSTRAINT chk_receipt_exclusive_target " +
                    "CHECK ((invoice_id IS NOT NULL AND contribution_id IS NULL) OR " +
                    "(invoice_id IS NULL AND contribution_id IS NOT NULL))");

            System.out.println("XOR Constraint successfully added!");

            // Test 1: Valid Invoice Receipt
            try {
                stmt.execute("INSERT INTO receipts (receipt_code, invoice_id, contribution_id, payer_name, total_amount) " +
                        "VALUES ('REC-INV-001', 1, NULL, 'Nguyen Van A', 906000.00)");
                System.out.println("Test 1 PASS: Invoice-only receipt accepted.");
            } catch (Exception e) {
                System.out.println("Test 1 FAIL: " + e.getMessage());
            }

            // Test 2: Valid Contribution Receipt
            try {
                stmt.execute("INSERT INTO receipts (receipt_code, invoice_id, contribution_id, payer_name, total_amount) " +
                        "VALUES ('REC-FND-001', NULL, 1, 'Nguyen Van A', 500000.00)");
                System.out.println("Test 2 PASS: Contribution-only receipt accepted.");
            } catch (Exception e) {
                System.out.println("Test 2 FAIL: " + e.getMessage());
            }

            // Test 3: Bundled Receipt (Both invoice_id and contribution_id) -> MUST BE BLOCKED
            try {
                stmt.execute("INSERT INTO receipts (receipt_code, invoice_id, contribution_id, payer_name, total_amount) " +
                        "VALUES ('REC-BUNDLED', 1, 1, 'Nguyen Van A', 1406000.00)");
                System.out.println("Test 3 CRITICAL FAIL: Bundled receipt was accepted!");
            } catch (Exception e) {
                System.out.println("Test 3 PASS: Bundled receipt successfully blocked: " + e.getMessage());
            }

            // Test 4: Orphan Receipt (Neither invoice_id nor contribution_id) -> MUST BE BLOCKED
            try {
                stmt.execute("INSERT INTO receipts (receipt_code, invoice_id, contribution_id, payer_name, total_amount) " +
                        "VALUES ('REC-ORPHAN', NULL, NULL, 'Nguyen Van A', 100000.00)");
                System.out.println("Test 4 CRITICAL FAIL: Orphan receipt was accepted!");
            } catch (Exception e) {
                System.out.println("Test 4 PASS: Orphan receipt successfully blocked: " + e.getMessage());
            }
        }
    }
}
