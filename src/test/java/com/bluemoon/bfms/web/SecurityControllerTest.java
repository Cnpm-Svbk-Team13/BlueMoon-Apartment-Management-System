package com.bluemoon.bfms.web;

import com.bluemoon.bfms.repository.AuditLogRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Kiểm tra RBAC và form login ở tầng Spring Security bằng H2.
 *
 * <p>H2 chứng minh hành vi MVC/Security cô lập; PostgreSQL lockout/DDL vẫn
 * cần kiểm tra riêng theo profile nghiệp vụ.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuditLogRepository auditLogs;

    @BeforeEach
    void seedAccounts() {
        jdbc.execute("""
                CREATE ALIAS IF NOT EXISTS PG_ADVISORY_XACT_LOCK
                AS 'void lock(int key1, int key2) {}'
                """);
        insertAccount(501L, "admin_test", "ROLE_ADMIN", true);
        insertAccount(502L, "accountant_test", "ROLE_ACCOUNTANT", true);
        insertAccount(503L, "cashier_test", "ROLE_CASHIER", true);
        insertAccount(504L, "inactive_test", "ROLE_ACCOUNTANT", false);
    }

    private void insertAccount(Long id, String username, String role, boolean active) {
        String email = username + "@example.test";
        jdbc.update(
                "INSERT INTO persons (full_name, phone, email) VALUES (?, ?, ?)",
                "Test " + username,
                "0900000000",
                email);
        Long personId = jdbc.queryForObject(
                "SELECT id FROM persons WHERE email = ?",
                Long.class,
                email);
        jdbc.update("""
                INSERT INTO users
                    (id, username, password_hash, person_id, role,
                     failed_attempts, locked_until, is_active)
                VALUES (?, ?, ?, ?, ?, 0, NULL, ?)
                """,
                id,
                username,
                new BCryptPasswordEncoder(10).encode("Password@123"),
                personId,
                role,
                active);
    }

    @Test
    void anonymousUserIsRedirectedToLogin() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 502, username = "accountant_test", authorities = "ROLE_ACCOUNTANT")
    void accountantCannotAccessAdminUserRoutes() throws Exception {
        mvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanAccessAdminDashboard() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void validLoginCreatesSessionAndRedirectsAdmin() throws Exception {
        mvc.perform(post("/login").with(csrf())
                        .param("username", "admin_test")
                        .param("password", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    void loggedInActorKeepsOriginalIdWhenOldUsernameIsReused() throws Exception {
        var login = mvc.perform(post("/login").with(csrf())
                        .param("username", "admin_test")
                        .param("password", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        assertNotNull(session);

        mvc.perform(post("/admin/users/501").session(session).with(csrf())
                        .param("username", "renamed_admin")
                        .param("fullName", "Test admin_test")
                        .param("email", "admin_test@example.test")
                        .param("phone", "0900000000")
                        .param("role", "ROLE_ADMIN")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection());

        mvc.perform(post("/admin/users/502").session(session).with(csrf())
                        .param("username", "admin_test")
                        .param("fullName", "Test accountant_test")
                        .param("email", "accountant_test@example.test")
                        .param("phone", "0900000000")
                        .param("role", "ROLE_ADMIN")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection());

        mvc.perform(post("/admin/users").session(session).with(csrf())
                        .param("username", "audit_probe")
                        .param("fullName", "Audit Probe")
                        .param("email", "audit_probe@example.test")
                        .param("phone", "0901234567")
                        .param("role", "ROLE_ACCOUNTANT")
                        .param("password", "Password@123")
                        .param("confirmPassword", "Password@123"))
                .andExpect(status().is3xxRedirection());

        Long createdUserId = jdbc.queryForObject(
                "SELECT id FROM users WHERE username = 'audit_probe'", Long.class);
        var creationAudit = auditLogs.findAllByEntityNameAndEntityIdOrderByCreatedAtDescIdDesc(
                        "User", createdUserId).stream()
                .filter(audit -> "CREATE_USER".equals(audit.getAction()))
                .findFirst()
                .orElseThrow();
        assertEquals(501L, creationAudit.getUserId());
    }

    @Test
    void invalidLoginUsesGenericFailureRedirect() throws Exception {
        mvc.perform(post("/login").with(csrf())
                        .param("username", "unknown_test")
                        .param("password", "wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 503, username = "cashier_test", authorities = "ROLE_CASHIER")
    void cashierCanAccessPortal() throws Exception {
        mvc.perform(get("/portal"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanCreateAccountAndRedirect() throws Exception {
        mvc.perform(post("/admin/users").with(csrf())
                        .param("username", "new_account")
                        .param("fullName", "Tài Khoản Mới")
                        .param("email", "new_account@example.test")
                        .param("phone", "0901234567")
                        .param("role", "ROLE_ACCOUNTANT")
                        .param("password", "Password@123")
                        .param("confirmPassword", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanSetAccountStatusUsingFrontendContract() throws Exception {
        mvc.perform(post("/admin/users/502/deactivate").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        mvc.perform(post("/admin/users/502/activate").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        mvc.perform(post("/admin/users/502/status").with(csrf())
                        .param("active", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        mvc.perform(post("/admin/users/502/status").with(csrf())
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanUpdateAccount() throws Exception {
        mvc.perform(post("/admin/users/502").with(csrf())
                        .param("username", "accountant_test")
                        .param("fullName", "Kế Toán Đã Cập Nhật")
                        .param("email", "accountant_test@example.test")
                        .param("phone", "0912345678")
                        .param("role", "ROLE_ACCOUNTANT")
                        .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void editingWithUncheckedActiveCheckboxDisablesAccount() throws Exception {
        mvc.perform(post("/admin/users/502").with(csrf())
                        .param("username", "accountant_test")
                        .param("fullName", "Kế Toán Đã Cập Nhật")
                        .param("email", "accountant_test@example.test")
                        .param("phone", "0912345678")
                        .param("role", "ROLE_ACCOUNTANT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        entityManager.flush();
        assertFalse(jdbc.queryForObject(
                "SELECT is_active FROM users WHERE id = 502", Boolean.class));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanLoadEditFormUsingFrontendModelAttribute() throws Exception {
        mvc.perform(get("/admin/users/502/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userId", 502L))
                .andExpect(view().name("admin/user-form"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void accountCreationRejectsMismatchedPasswords() throws Exception {
        mvc.perform(post("/admin/users").with(csrf())
                        .param("username", "new_account")
                        .param("fullName", "Tài Khoản Mới")
                        .param("email", "new_account@example.test")
                        .param("phone", "0901234567")
                        .param("role", "ROLE_ACCOUNTANT")
                        .param("password", "Password@123")
                        .param("confirmPassword", "Different@123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user-form"))
                .andExpect(model().attribute("errorMessage", "Hai mật khẩu chưa khớp."));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void adminCanResetAccountPassword() throws Exception {
        mvc.perform(post("/admin/users/502/reset-password").with(csrf())
                        .param("newPassword", "Replacement@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 501, username = "admin_test", authorities = "ROLE_ADMIN")
    void authenticatedUserCanChangeOwnPassword() throws Exception {
        mvc.perform(post("/admin/users/change-password").with(csrf())
                        .param("currentPassword", "Password@123")
                        .param("newPassword", "Changed@123")
                        .param("confirmPassword", "Changed@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithAuthenticatedAccount(userId = 503, username = "cashier_test", authorities = "ROLE_CASHIER")
    void cashierCanOpenPasswordChangeRouteFromFrontend() throws Exception {
        mvc.perform(get("/admin/users/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/change-password"));
    }

    @Test
    void loginPageIsAvailableWithoutAuthentication() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthenticatedAccount(userId = 503, username = "cashier_test", authorities = "ROLE_CASHIER")
    void logoutUsesFrontendSuccessParameter() throws Exception {
        mvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    void inactiveAccountCannotLogin() throws Exception {
        mvc.perform(post("/login").with(csrf())
                        .param("username", "inactive_test")
                        .param("password", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void fifthWrongPasswordLocksAccount() throws Exception {
        for (int attempt = 1; attempt <= 5; attempt++) {
            mvc.perform(post("/login").with(csrf())
                            .param("username", "accountant_test")
                            .param("password", "wrong-password"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error"));
        }

        mvc.perform(post("/login").with(csrf())
                        .param("username", "accountant_test")
                        .param("password", "Password@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}
