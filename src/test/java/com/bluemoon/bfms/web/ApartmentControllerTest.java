package com.bluemoon.bfms.web;

import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

/** Exercises BE-02, US-001 AC 1-2 and NFR-02 through MVC, Thymeleaf and H2. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "son", roles = "ADMIN")
class ApartmentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ApartmentRepository apartments;
    @Autowired
    private AuditLogRepository audits;

    @BeforeEach
    void seedActors() {
        // Synthetic authentication fixtures; the application does not map users yet.
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY, username VARCHAR(50), role VARCHAR(20), is_active BOOLEAN
                )
                """);
        jdbc.update("DELETE FROM users");
        jdbc.update("INSERT INTO users VALUES (7, 'son', 'ROLE_ADMIN', TRUE)");
        jdbc.update("INSERT INTO users VALUES (8, 'accountant', 'ROLE_ACCOUNTANT', TRUE)");
        jdbc.update("INSERT INTO users VALUES (9, 'cashier', 'ROLE_CASHIER', TRUE)");
        jdbc.update("INSERT INTO users VALUES (10, 'disabled', 'ROLE_ADMIN', FALSE)");
    }

    private MockHttpServletRequestBuilder creation(String room, String area, String type) {
        return post("/admin/apartments").with(csrf())
                .param("roomNumber", room).param("floorNumber", "6")
                .param("areaSqm", area).param("apartmentType", type);
    }

    @Test
    void rendersSelectAndCsrfToken() throws Exception {
        mvc.perform(get("/admin/apartments/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<select")))
                .andExpect(content().string(containsString("RESIDENTIAL")))
                .andExpect(content().string(containsString("name=\"_csrf\"")));
    }

    @Test
    void createsApartmentAndAuditAndRedirectsAfterPost() throws Exception {
        mvc.perform(creation(" web0601 ", "75.50", "RESIDENTIAL")
                        .param("currentUserId", "999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/apartments"))
                .andExpect(flash().attributeExists("successMessage"));
        var apartment = apartments.findByRoomNumber("WEB0601").orElseThrow();
        var audit = audits.findAll().stream()
                .filter(record -> apartment.getId().equals(record.getEntityId())).findFirst().orElseThrow();
        assertEquals(7L, audit.getUserId());
        assertEquals("CREATE", audit.getAction());
        assertEquals("Apartment", audit.getEntityName());
    }

    @Test
    void reportsDuplicateOnRoomFieldAndKeepsInput() throws Exception {
        mvc.perform(creation("WEB0602", "75.50", "RESIDENTIAL"))
                .andExpect(status().is3xxRedirection());
        long auditCount = audits.count();
        mvc.perform(creation("web0602", "80.50", "COMMERCIAL"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("apartmentForm", "roomNumber"))
                .andExpect(content().string(containsString("Room number already exists.")))
                .andExpect(content().string(containsString("80.50")));
        assertEquals(auditCount, audits.count());
    }

    @Test
    void rejectsZeroAreaAndInvalidEnum() throws Exception {
        mvc.perform(creation("WEB-BAD", "0", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("apartmentForm", "areaSqm", "apartmentType"));
        assertFalse(apartments.existsByRoomNumber("WEB-BAD"));
    }

    @Test
    void rejectsExcessivePrecisionAndOutOfRangeFloor() throws Exception {
        mvc.perform(post("/admin/apartments").with(csrf())
                        .param("roomNumber", "WEB-BAD").param("areaSqm", "99999999999.123")
                        .param("apartmentType", "RESIDENTIAL").param("floorNumber", "31"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("apartmentForm", "areaSqm", "floorNumber"));
        assertFalse(apartments.existsByRoomNumber("WEB-BAD"));
    }

    @Test
    @WithMockUser(username = "accountant", roles = "ACCOUNTANT")
    void permitsActiveAccountant() throws Exception {
        mvc.perform(creation("ACCOUNTANT-TEST", "75.50", "KIOT"))
                .andExpect(status().is3xxRedirection());
        assertTrue(apartments.existsByRoomNumber("ACCOUNTANT-TEST"));
    }

    @Test
    @WithMockUser(username = "cashier", roles = "ADMIN")
    void checksDatabaseRoleEvenWhenPrincipalClaimsAdmin() throws Exception {
        mvc.perform(creation("FORBIDDEN-TEST", "75.50", "RESIDENTIAL"))
                .andExpect(status().isForbidden());
        assertFalse(apartments.existsByRoomNumber("FORBIDDEN-TEST"));
    }

    @Test
    @WithMockUser(username = "disabled", roles = "ADMIN")
    void rejectsDisabledAccount() throws Exception {
        mvc.perform(get("/admin/apartments/new")).andExpect(status().isForbidden());
    }

    @Test
    void requiresCsrfForCreation() throws Exception {
        mvc.perform(post("/admin/apartments")).andExpect(status().isForbidden());
    }

    @Test
    void listsByFloorAndRejectsInvalidFilter() throws Exception {
        mvc.perform(creation("FLOOR-SIX", "75.50", "KIOT")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/admin/apartments").param("floor", "6"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("FLOOR-SIX")));
        mvc.perform(get("/admin/apartments"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("FLOOR-SIX")));
        mvc.perform(get("/admin/apartments").param("floor", "31")).andExpect(status().isBadRequest());
    }

    @Test
    void editsWithoutChangingRoomNumberAndWritesAudit() throws Exception {
        mvc.perform(creation("EDIT-SIX", "75.50", "KIOT")).andExpect(status().is3xxRedirection());
        Long id = apartments.findByRoomNumber("EDIT-SIX").orElseThrow().getId();
        mvc.perform(get("/admin/apartments/{id}/edit", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Edit apartment")));
        mvc.perform(post("/admin/apartments/{id}", id).with(csrf())
                        .param("roomNumber", "EDIT-SIX").param("floorNumber", "6")
                        .param("areaSqm", "80.00").param("apartmentType", "KIOT")
                        .param("status", "OCCUPIED").param("description", "Updated details"))
                .andExpect(redirectedUrl("/admin/apartments"));
        assertEquals("OCCUPIED", apartments.findById(id).orElseThrow().getStatus().name());
        assertTrue(audits.findAll().stream().anyMatch(log ->
                id.equals(log.getEntityId()) && "UPDATE".equals(log.getAction())));
        mvc.perform(get("/admin/apartments/{id}/edit", -1)).andExpect(status().isNotFound());
    }

    @Test
    void blocksRenamingAnApartmentToAnotherRoomNumber() throws Exception {
        mvc.perform(creation("EDIT-ONE", "75.50", "KIOT")).andExpect(status().is3xxRedirection());
        mvc.perform(creation("EDIT-TWO", "75.50", "KIOT")).andExpect(status().is3xxRedirection());
        Long id = apartments.findByRoomNumber("EDIT-ONE").orElseThrow().getId();
        mvc.perform(post("/admin/apartments/{id}", id).with(csrf())
                        .param("roomNumber", "edit-two").param("floorNumber", "6")
                        .param("areaSqm", "80.00").param("apartmentType", "KIOT"))
                .andExpect(model().attributeHasFieldErrors("apartmentForm", "roomNumber"));
        assertEquals("EDIT-ONE", apartments.findById(id).orElseThrow().getRoomNumber());
    }
}
