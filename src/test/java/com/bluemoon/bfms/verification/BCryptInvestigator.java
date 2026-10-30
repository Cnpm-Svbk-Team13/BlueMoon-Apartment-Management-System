package com.bluemoon.bfms.verification;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptInvestigator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String proposedHash = "$2a$10$7EqJtq98hPqEX7fNZaFWoO.8/k0aT9iP72VfM3bWqK/XQ3yOqS8re";

        String[] candidates = {
            "Password@123", "password@123", "Password123", "password123",
            "admin", "admin123", "Admin@123", "123456", "Password",
            "bluemoon", "BlueMoon@123", "BlueMoon123", "secret", "password"
        };

        for (String c : candidates) {
            try {
                if (encoder.matches(c, proposedHash)) {
                    System.out.println("FOUND MATCH: " + c);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error for " + c + ": " + e.getMessage());
            }
        }
        System.out.println("No match found among common candidates. Proposed hash is completely invalid for Password@123!");
    }
}
