package com.bluemoon.bfms.verification;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class H2RegexTester {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:test_regex;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE residents (id BIGSERIAL PRIMARY KEY, national_id VARCHAR(20))");
            try {
                stmt.execute("ALTER TABLE residents ADD CONSTRAINT chk_national_id_format " +
                        "CHECK (national_id IS NULL OR national_id ~ '^[0-9]{12}$')");
                System.out.println("H2 supports PostgreSQL ~ regex check constraint!");
            } catch (Exception e) {
                System.out.println("H2 FAILED PostgreSQL ~ syntax: " + e.getMessage());
            }
        }
    }
}
