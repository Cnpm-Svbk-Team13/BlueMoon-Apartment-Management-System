package com.bluemoon.bfms.web;

import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.HouseholdHeadHistoryRepository;
import com.bluemoon.bfms.repository.HouseholdRepository;
import com.bluemoon.bfms.model.HouseholdStatus;
import com.bluemoon.bfms.repository.HouseholdWriteLock;
import com.bluemoon.bfms.repository.PersonRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Verifies BE-03, US-001 AC 3-4 and RAW-002 with MVC and H2 fixtures. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "head-editor", roles = "ADMIN")
class HouseholdHeadWebTest {
    @Autowired private MockMvc mvc;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private ApartmentRepository apartments;
    @Autowired private PersonRepository persons;
    @Autowired private HouseholdHeadHistoryRepository history;
    @Autowired private HouseholdRepository households;
    @Autowired private EntityManager entityManager;

    @MockitoBean
    private HouseholdWriteLock writeLock;

    private Long apartmentId;
    private String url;

    @BeforeEach
    void seed() {
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT PRIMARY KEY, username VARCHAR(50), role VARCHAR(20), is_active BOOLEAN
                )
                """);
        jdbc.update("INSERT INTO users VALUES (97, 'head-editor', 'ROLE_ADMIN', TRUE)");
        apartmentId = apartments.save(new Apartment("HEAD-TEST", 6,
                new BigDecimal("75.50"), ApartmentType.RESIDENTIAL)).getId();
        url = "/admin/apartments/" + apartmentId + "/household";
    }

    @Test
    void assignsReplacesAndUpdatesProfileWithoutOverwritingSnapshots() throws Exception {
        mvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Assign first head")));
        assignFirst();
        Long firstId = persons.findByNationalId("000000000001").orElseThrow().getId();

        mvc.perform(headPost(url, "000000000002", "Next Head")
                        .param("expectedHeadPersonId", firstId.toString()))
                .andExpect(redirectedUrl(url));
        Long nextId = persons.findByNationalId("000000000002").orElseThrow().getId();
        mvc.perform(get(url).param("editProfile", "true")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Next Head")));
        mvc.perform(headPost(url + "/profile", "000000000002", "Updated Head")
                        .param("expectedHeadPersonId", nextId.toString()))
                .andExpect(redirectedUrl(url));

        var entries = history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(apartmentId);
        assertEquals(3, entries.size());
        assertEquals("Next Head", entries.get(0).getPreviousSnapshot().get("full_name").asText());
        assertEquals("First Head", entries.get(1).getPreviousSnapshot().get("full_name").asText());
        assertEquals(97L, entries.get(0).getChangedByUserId());
        assertEquals("First Head", persons.findById(firstId).orElseThrow().getFullName());
        mvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(content().string(containsString("PROFILE_UPDATED")))
                .andExpect(content().string(containsString("First Head")));
    }

    @Test
    void reusesConfirmedPersonWithoutOverwritingProfile() throws Exception {
        Person existing = persons.save(new Person("Existing Person", "000000000003", "0123456789", null));
        mvc.perform(post(url + "/lookup").with(csrf())
                        .param("headPerson.nationalId", "000000000003"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Confirm use of the existing profile")));
        mvc.perform(headPost(url, "000000000003", "Different Submitted Name")
                        .param("movedInDate", "2026-11-01")
                        .param("confirmedPersonId", existing.getId().toString()))
                .andExpect(redirectedUrl(url));
        var entry = history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(apartmentId).getFirst();
        assertEquals(existing.getId(), entry.getNewHeadPerson().getId());
        assertEquals("Existing Person", entry.getNewSnapshot().get("full_name").asText());
    }

    @Test
    void rejectsInvalidNationalIdAndPhone() throws Exception {
        mvc.perform(post(url).with(csrf()).param("headPerson.fullName", "Invalid Profile")
                        .param("headPerson.nationalId", "123").param("headPerson.phone", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors(
                        "headForm", "headPerson.nationalId", "headPerson.phone"));
        assertTrue(history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(apartmentId).isEmpty());
    }

    @Test
    void rejectsStaleAssignmentAfterFirstHeadWasSaved() throws Exception {
        assignFirst();
        mvc.perform(headPost(url, "000000000004", "Stale Head")
                        .param("movedInDate", "2026-11-01"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The household head has changed.")));
        assertTrue(persons.findByNationalId("000000000004").isEmpty());
    }

    @Test
    void updatesBothResidentRelationshipsWhenReplacingHead() throws Exception {
        assignFirst();
        Long oldId = persons.findByNationalId("000000000001").orElseThrow().getId();
        Long nextId = persons.save(new Person("Next Resident", "000000000005", "0123456789", null)).getId();
        Long householdId = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE)
                .orElseThrow().getId();
        jdbc.update("""
                INSERT INTO residents (person_id, household_id, relationship_with_head, residence_status)
                VALUES (?, ?, 'HEAD', 'THUONG_TRU'), (?, ?, 'CHILD', 'THUONG_TRU')
                """, oldId, householdId, nextId, householdId);

        mvc.perform(headPost(url, "000000000005", "Next Resident")
                        .param("confirmedPersonId", nextId.toString())
                        .param("expectedHeadPersonId", oldId.toString())
                        .param("previousHeadRelationship", "PARENT"))
                .andExpect(redirectedUrl(url));

        entityManager.flush();
        assertEquals("PARENT", jdbc.queryForObject(
                "SELECT relationship_with_head FROM residents WHERE person_id = ?", String.class, oldId));
        assertEquals("HEAD", jdbc.queryForObject(
                "SELECT relationship_with_head FROM residents WHERE person_id = ?", String.class, nextId));
    }

    private void assignFirst() throws Exception {
        mvc.perform(headPost(url, "000000000001", "First Head")
                        .param("movedInDate", "2026-11-01"))
                .andExpect(redirectedUrl(url));
    }

    private MockHttpServletRequestBuilder headPost(String target, String nationalId, String name) {
        return post(target).with(csrf()).param("headPerson.nationalId", nationalId)
                .param("headPerson.fullName", name).param("headPerson.phone", "0123456789")
                .param("headPerson.email", "");
    }
}
