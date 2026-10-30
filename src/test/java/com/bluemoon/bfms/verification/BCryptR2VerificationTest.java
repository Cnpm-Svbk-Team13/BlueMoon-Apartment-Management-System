package com.bluemoon.bfms.verification;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptR2VerificationTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "Password@123";
        String remediatedHash = "$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK";

        System.out.println("=== BCRYPT R2 EMPIRICAL VERIFICATION ===");
        System.out.println("Testing raw password: " + rawPassword);
        System.out.println("Testing remediated hash: " + remediatedHash);

        boolean matchesRemediated = false;
        try {
            matchesRemediated = encoder.matches(rawPassword, remediatedHash);
            System.out.println("Remediated hash matches Password@123: " + matchesRemediated);
        } catch (Exception e) {
            System.out.println("Remediated hash error: " + e.getMessage());
        }

        boolean matchesWrong = encoder.matches("WrongPassword!123", remediatedHash);
        System.out.println("Remediated hash matches WrongPassword: " + matchesWrong);

        boolean matchesEmpty = encoder.matches("", remediatedHash);
        System.out.println("Remediated hash matches empty password: " + matchesEmpty);

        if (matchesRemediated && !matchesWrong && !matchesEmpty) {
            System.out.println("RESULT: BCRYPT_R2_VERIFICATION_PASS");
        } else {
            System.out.println("RESULT: BCRYPT_R2_VERIFICATION_FAIL");
        }
    }
}
