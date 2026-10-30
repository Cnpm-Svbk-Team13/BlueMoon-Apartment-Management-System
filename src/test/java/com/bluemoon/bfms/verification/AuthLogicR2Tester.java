package com.bluemoon.bfms.verification;

import java.time.ZonedDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthLogicR2Tester {

    static class DisabledException extends RuntimeException {
        public DisabledException(String message) {
            super(message);
        }
    }

    static class User {
        private String username;
        private String passwordHash;
        private int failedAttempts = 0;
        private ZonedDateTime lockedUntil = null;
        private boolean isActive = true;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPasswordHash() { return passwordHash; }
        public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
        public int getFailedAttempts() { return failedAttempts; }
        public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
        public ZonedDateTime getLockedUntil() { return lockedUntil; }
        public void setLockedUntil(ZonedDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
        public boolean getIsActive() { return isActive; }
        public void setIsActive(boolean isActive) { this.isActive = isActive; }
    }

    enum LoginResult {
        USER_NOT_FOUND, ACCOUNT_LOCKED, BAD_CREDENTIALS, LOCKED_NOW, SUCCESS
    }

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Exact implementation from docs/KE_HOACH_CHI_TIET_SPRINT_1.md:279-321
    public static LoginResult authenticate(User user, String password) {
        // Nút 1: Bắt đầu, tìm kiếm người dùng theo username
        if (user == null) { // Nút 2: Rẽ nhánh kiểm tra user tồn tại
            return LoginResult.USER_NOT_FOUND; // Nút 3: Thoát - Không tìm thấy
        }

        // Kiểm tra trạng thái hoạt động (Chống cán bộ bị đình chỉ/vô hiệu hóa truy cập trái phép)
        if (!user.getIsActive()) throw new DisabledException("Tài khoản đã bị vô hiệu hóa");

        // Xử lý tự động giải phóng khóa khi hết thời hạn (Reset counter on lockout expiry)
        ZonedDateTime now = ZonedDateTime.now();
        if (user.getLockedUntil() != null && user.getLockedUntil().isBefore(now)) {
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
        }
        
        // Nút 4: Kiểm tra tài khoản có đang trong thời gian bị khóa hay không
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(now)) {
            return LoginResult.ACCOUNT_LOCKED; // Nút 5: Thoát - Tài khoản bị khóa
        }
        
        // Nút 6: So khớp mật khẩu băm BCrypt
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            // Nút 7: Mật khẩu sai, tăng biến đếm
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);
            if (attempts >= 5) { // Nút 8: Rẽ nhánh kiểm tra mốc 5 lần sai
                user.setLockedUntil(now.plusMinutes(15)); // Nút 9: Khóa 15 phút
                return LoginResult.LOCKED_NOW; // Nút 10: Thoát - Bị khóa ngay lập tức
            } else {
                return LoginResult.BAD_CREDENTIALS; // Nút 11: Thoát - Sai mật khẩu
            }
        }
        
        // Nút 12: Mật khẩu đúng, đặt lại số lần sai
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        return LoginResult.SUCCESS; // Nút 13: Thoát - Thành công
    }

    public static void main(String[] args) {
        System.out.println("=== TESTING REMEDIATED AUTH LOGIC ===");
        String validHash = "$2a$10$ThEK5Lsf49T189/5nvFzwuYgdsmzBFWloKeb2FM2QSwB4JsNK2uTK";

        // Test 1: Inactive User
        User inactiveUser = new User();
        inactiveUser.setUsername("fired_staff");
        inactiveUser.setPasswordHash(validHash);
        inactiveUser.setIsActive(false);

        boolean inactiveBlocked = false;
        try {
            authenticate(inactiveUser, "Password@123");
        } catch (DisabledException e) {
            inactiveBlocked = true;
            System.out.println("Test 1 PASS: Inactive user blocked with exception: " + e.getMessage());
        }
        if (!inactiveBlocked) {
            System.out.println("Test 1 FAIL: Inactive user was NOT blocked!");
        }

        // Test 2: Expired lockout single typo does NOT re-lock immediately
        User expiredUser = new User();
        expiredUser.setUsername("staff");
        expiredUser.setPasswordHash(validHash);
        expiredUser.setFailedAttempts(5);
        expiredUser.setLockedUntil(ZonedDateTime.now().minusMinutes(2));

        LoginResult res2 = authenticate(expiredUser, "WrongPasswordOnce");
        System.out.println("Test 2 result on single typo after lockout expired: " + res2);
        System.out.println("Failed attempts count: " + expiredUser.getFailedAttempts());
        System.out.println("Locked until: " + expiredUser.getLockedUntil());
        if (res2 == LoginResult.BAD_CREDENTIALS && expiredUser.getFailedAttempts() == 1 && expiredUser.getLockedUntil() == null) {
            System.out.println("Test 2 PASS: Counter reset cleanly; user given fresh 5 attempts!");
        } else {
            System.out.println("Test 2 FAIL: Counter was not properly reset after lockout expired!");
        }

        // Test 3: Expired lockout with correct password -> SUCCESS
        User expiredUserSuccess = new User();
        expiredUserSuccess.setUsername("staff2");
        expiredUserSuccess.setPasswordHash(validHash);
        expiredUserSuccess.setFailedAttempts(5);
        expiredUserSuccess.setLockedUntil(ZonedDateTime.now().minusMinutes(1));

        LoginResult res3 = authenticate(expiredUserSuccess, "Password@123");
        if (res3 == LoginResult.SUCCESS && expiredUserSuccess.getFailedAttempts() == 0 && expiredUserSuccess.getLockedUntil() == null) {
            System.out.println("Test 3 PASS: Post-expiry correct login succeeds immediately.");
        } else {
            System.out.println("Test 3 FAIL: Post-expiry correct login failed: " + res3);
        }

        // Test 4: Active lockout period -> ACCOUNT_LOCKED
        User lockedUser = new User();
        lockedUser.setUsername("locked_guy");
        lockedUser.setPasswordHash(validHash);
        lockedUser.setFailedAttempts(5);
        lockedUser.setLockedUntil(ZonedDateTime.now().plusMinutes(10));

        LoginResult res4 = authenticate(lockedUser, "Password@123");
        if (res4 == LoginResult.ACCOUNT_LOCKED) {
            System.out.println("Test 4 PASS: Active lockout blocks authentication.");
        } else {
            System.out.println("Test 4 FAIL: Active lockout did not block: " + res4);
        }

        // Test 5: Null user -> USER_NOT_FOUND
        LoginResult res5 = authenticate(null, "Password@123");
        if (res5 == LoginResult.USER_NOT_FOUND) {
            System.out.println("Test 5 PASS: Null user returns USER_NOT_FOUND.");
        } else {
            System.out.println("Test 5 FAIL: Null user returned: " + res5);
        }

        // Test 6: Brute force progression (attempts 1 to 5)
        User bruteUser = new User();
        bruteUser.setUsername("victim_brute");
        bruteUser.setPasswordHash(validHash);
        for (int i = 1; i <= 4; i++) {
            LoginResult res = authenticate(bruteUser, "WrongPass" + i);
            if (res != LoginResult.BAD_CREDENTIALS || bruteUser.getFailedAttempts() != i) {
                System.out.println("Test 6 FAIL at attempt " + i + ": " + res);
            }
        }
        LoginResult res5th = authenticate(bruteUser, "WrongPass5");
        if (res5th == LoginResult.LOCKED_NOW && bruteUser.getFailedAttempts() == 5 && bruteUser.getLockedUntil() != null) {
            System.out.println("Test 6 PASS: Brute force progression locks on 5th attempt.");
        } else {
            System.out.println("Test 6 FAIL on 5th attempt: " + res5th);
        }

        // Test 7: Normal successful login
        User normalUser = new User();
        normalUser.setUsername("admin");
        normalUser.setPasswordHash(validHash);
        normalUser.setFailedAttempts(2);
        LoginResult res7 = authenticate(normalUser, "Password@123");
        if (res7 == LoginResult.SUCCESS && normalUser.getFailedAttempts() == 0) {
            System.out.println("Test 7 PASS: Normal login succeeds and resets failed attempts.");
        } else {
            System.out.println("Test 7 FAIL: Normal login: " + res7);
        }

        System.out.println("=== ALL AUTH LOGIC TESTS COMPLETED ===");
    }
}
