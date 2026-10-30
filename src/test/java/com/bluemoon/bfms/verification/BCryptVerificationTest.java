package com.bluemoon.bfms.verification;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptVerificationTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "Password@123";
        String proposedHash = "$2a$10$7EqJtq98hPqEX7fNZaFWoO.8/k0aT9iP72VfM3bWqK/XQ3yOqS8re";
        String seedFakeHash = "$2a$10$7j4m7Uq3vW0B8mGZ2Yk0k.k8eWjW9E1W5X6Q2J4K8M0O2P4R6T8V0";

        System.out.println("=== BCRYPT EMPIRICAL VERIFICATION ===");
        System.out.println("Testing raw password: " + rawPassword);

        // 1. Check proposed hash in report
        boolean matchesProposed = false;
        try {
            matchesProposed = encoder.matches(rawPassword, proposedHash);
            System.out.println("Proposed hash matches: " + matchesProposed);
        } catch (Exception e) {
            System.out.println("Proposed hash error: " + e.getMessage());
        }

        // 2. Check fake hash in seed_data.sql
        boolean matchesSeedFake = false;
        try {
            matchesSeedFake = encoder.matches(rawPassword, seedFakeHash);
            System.out.println("Seed fake hash matches: " + matchesSeedFake);
        } catch (Exception e) {
            System.out.println("Seed fake hash error: " + e.getMessage());
        }

        // 3. Test wrong password against proposed hash
        boolean matchesWrong = encoder.matches("WrongPassword!123", proposedHash);
        System.out.println("Wrong password matches: " + matchesWrong);

        // 4. Generate fresh hash and verify
        String freshHash = encoder.encode(rawPassword);
        System.out.println("Fresh generated hash: " + freshHash);
        System.out.println("Fresh hash matches: " + encoder.matches(rawPassword, freshHash));

        if (matchesProposed && !matchesSeedFake && !matchesWrong) {
            System.out.println("RESULT: BCRYPT_VERIFICATION_PASS");
        } else {
            System.out.println("RESULT: BCRYPT_VERIFICATION_FAIL");
        }
    }
}
