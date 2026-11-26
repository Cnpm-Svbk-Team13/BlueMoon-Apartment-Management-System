package com.bluemoon.bfms.fixture;

import static com.bluemoon.bfms.fixture.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.core.JdbcTemplate;

class FixtureSmokeIT {
    static final JdbcTemplate owner = PgContainers.ownerJdbc(PgContainers.PLAIN);
    static final JdbcFixtures fx = new JdbcFixtures(owner);

    static Stream<Arguments> badFloors() {
        return Stream.of(Arguments.of(0), Arguments.of(31));
    }

    static Stream<Arguments> badAreas() {
        return Stream.of(Arguments.of(AREA_ZERO), Arguments.of(AREA_NEGATIVE),
                Arguments.of(AREA_ROUNDS_TO_ZERO));
    }

    @ParameterizedTest
    @MethodSource("badFloors")
    void floorOutOfRange_violatesCheck(int floor) {
        assertThatThrownBy(() -> fx.apartment(uniqueRoom(), floor, AREA_TYPICAL, TYPE_RESIDENTIAL, APT_VACANT))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.CHECK_VIOLATION));
    }

    @ParameterizedTest
    @MethodSource("badAreas")
    void nonPositiveArea_violatesCheck(BigDecimal area) {
        assertThatThrownBy(() -> fx.apartment(uniqueRoom(), 5, area, TYPE_RESIDENTIAL, APT_VACANT))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.CHECK_VIOLATION));
    }

    @Test
    void floorBoundaries_areAccepted() {
        fx.apartment(uniqueRoom(), FLOOR_MIN, AREA_SMALLEST_VALID, TYPE_KIOT, APT_VACANT);
        fx.apartment(uniqueRoom(), FLOOR_MAX, AREA_MAX_VALID, TYPE_PENTHOUSE, APT_VACANT);
    }

    @Test
    void headWithoutPhone_isRejected() {
        long noPhone = fx.person("Thiếu SĐT", uniqueNationalId(), null, null, null, null);
        long apt = fx.apartment();
        assertThatThrownBy(() -> fx.household(apt, noPhone))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.CHECK_VIOLATION));
    }

    @Test
    void occupiedHouseholdAndHistory_areAccepted() {
        var h = fx.occupiedHousehold();
        long actor = fx.admin();
        fx.headHistory(h.householdId(), null, h.headPersonId(), actor, java.time.OffsetDateTime.now());
    }

    @Test
    void historyIsAppendOnly_andAppRoleHasNoDeleteOrUpdate() {
        var app = PgContainers.appJdbc(PgContainers.PLAIN);
        assertThatThrownBy(() -> owner.update("UPDATE audit_logs SET details = 'x'"))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.CHECK_VIOLATION));
        assertThatThrownBy(() -> app.update("UPDATE audit_logs SET details = 'x'"))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.INSUFFICIENT_PRIVILEGE));
        assertThatThrownBy(() -> app.update("DELETE FROM persons"))
                .satisfies(e -> assertThat(JdbcFixtures.sqlStateOf(e)).isEqualTo(SqlState.INSUFFICIENT_PRIVILEGE));
    }

    @Test
    void seededDatabase_matchesConstants() {
        var seededFx = new JdbcFixtures(PgContainers.ownerJdbc(PgContainers.seeded()));
        assertThat(seededFx.count("persons")).isEqualTo(COUNT_PERSONS);
        assertThat(seededFx.count("users")).isEqualTo(COUNT_USERS);
        assertThat(seededFx.count("apartments")).isEqualTo(COUNT_APARTMENTS);
        assertThat(seededFx.count("households")).isEqualTo(COUNT_HOUSEHOLDS);
        assertThat(seededFx.count("residents")).isEqualTo(COUNT_RESIDENTS);
        assertThat(seededFx.count("household_head_history")).isEqualTo(COUNT_HEAD_HISTORY);
        assertThat(seededFx.count("audit_logs")).isEqualTo(COUNT_AUDIT_LOGS);
    }
}
