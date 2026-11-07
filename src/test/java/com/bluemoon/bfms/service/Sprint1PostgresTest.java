package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.ApartmentForm;
import com.bluemoon.bfms.dto.HouseholdHeadForm;
import com.bluemoon.bfms.exception.DuplicateRoomNumberException;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.repository.HouseholdHeadHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/** Exercises real PostgreSQL constraints for BE-02/BE-03 and QA-05 (US-001, RAW-001/002). */
@EnabledIfEnvironmentVariable(named = "BLUEMOON_PG_TEST_URL",
        matches = "jdbc:postgresql://(?:localhost|127\\.0\\.0\\.1):[0-9]+/bluemoon_sprint1_test(?:_[a-z0-9]+)?")
@SpringBootTest(properties = {
        "spring.datasource.url=${BLUEMOON_PG_TEST_URL}",
        "spring.datasource.username=bluemoon_app",
        "spring.datasource.password=${BLUEMOON_PG_TEST_PASSWORD}",
        "spring.datasource.hikari.connection-init-sql=SET statement_timeout = '15s'",
        "spring.jpa.hibernate.ddl-auto=validate"
})
class Sprint1PostgresTest {
    @Autowired private ApartmentService apartments;
    @Autowired private ApartmentAccessService access;
    @Autowired private HouseholdHeadService heads;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private HouseholdHeadHistoryRepository history;

    @Test
    void concurrentApartmentCreatesKeepExactlyOneRoom() throws Exception {
        Long actorId = access.requireEditorId("admin");
        String roomNumber = roomNumber();
        List<RuntimeException> failures = race(
                () -> apartments.createApartment(apartmentForm(roomNumber), actorId, null),
                () -> apartments.createApartment(apartmentForm(roomNumber), actorId, null));

        assertEquals(1, failures.size());
        assertInstanceOf(DuplicateRoomNumberException.class, failures.getFirst());
        assertTrue(apartments.findByRoomNumber(roomNumber).isPresent());
    }

    @Test
    void concurrentAssignmentsAndReplacementsKeepOneActiveHouseholdAndValidHistory() throws Exception {
        Long actorId = access.requireEditorId("admin");
        Long id = apartments.createApartment(apartmentForm(roomNumber()), actorId, null).getId();
        var assigned = race(() -> heads.assignOrReplace(id, headForm(null), actorId, null),
                () -> heads.assignOrReplace(id, headForm(null), actorId, null));
        assertStaleRequest(assigned);
        Long oldHeadId = jdbc.queryForObject(
                "SELECT head_person_id FROM households WHERE apartment_id = ? AND status = 'ACTIVE'",
                Long.class, id);
        String oldNationalId = jdbc.queryForObject(
                "SELECT national_id FROM persons WHERE id = ?", String.class, oldHeadId);
        var replaced = race(() -> heads.assignOrReplace(id, headForm(oldHeadId), actorId, null),
                () -> heads.assignOrReplace(id, headForm(oldHeadId), actorId, null));
        assertStaleRequest(replaced);
        var entries = history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(id);
        assertEquals(2, entries.size());
        assertEquals(oldNationalId, entries.getFirst().getPreviousSnapshot().path("national_id").asText());
        assertEquals(1, jdbc.queryForObject(
                "SELECT count(*) FROM households WHERE apartment_id = ? AND status = 'ACTIVE'",
                Integer.class, id));
        assertNotNull(entries.getFirst().getChangedAt());
        assertEquals(actorId, entries.getFirst().getChangedByUserId());
        assertEquals("Synthetic Head", entries.getFirst().getNewSnapshot().path("full_name").asText());
    }

    private void assertStaleRequest(List<RuntimeException> failures) {
        assertEquals(1, failures.size());
        assertInstanceOf(IllegalArgumentException.class, failures.getFirst());
        assertTrue(failures.getFirst().getMessage().contains("The household head has changed."));
    }

    private List<RuntimeException> race(Runnable first, Runnable second) throws Exception {
        var executor = Executors.newFixedThreadPool(2);
        var start = new CountDownLatch(1);
        try {
            var left = executor.submit(() -> runAfter(start, first));
            var right = executor.submit(() -> runAfter(start, second));
            start.countDown();
            return java.util.stream.Stream.of(left.get(40, TimeUnit.SECONDS), right.get(40, TimeUnit.SECONDS))
                    .filter(java.util.Objects::nonNull).toList();
        } finally {
            executor.shutdownNow();
        }
    }

    private RuntimeException runAfter(CountDownLatch start, Runnable operation) throws InterruptedException {
        start.await();
        try {
            operation.run();
            return null;
        } catch (RuntimeException exception) {
            return exception;
        }
    }

    private String roomNumber() {
        return "S1-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase(Locale.ROOT);
    }

    private ApartmentForm apartmentForm(String roomNumber) {
        ApartmentForm form = new ApartmentForm();
        form.setRoomNumber(roomNumber);
        form.setFloorNumber(6);
        form.setApartmentType(ApartmentType.RESIDENTIAL);
        form.setAreaSqm(new BigDecimal("75.50"));
        return form;
    }

    private HouseholdHeadForm headForm(Long expectedId) {
        HouseholdHeadForm form = new HouseholdHeadForm();
        form.setExpectedHeadPersonId(expectedId);
        form.setMovedInDate(LocalDate.of(2026, 11, 1));
        form.getHeadPerson().setFullName("Synthetic Head");
        form.getHeadPerson().setNationalId(Long.toString(
                ThreadLocalRandom.current().nextLong(100_000_000_000L, 1_000_000_000_000L)));
        form.getHeadPerson().setPhone("0123456789");
        return form;
    }
}
