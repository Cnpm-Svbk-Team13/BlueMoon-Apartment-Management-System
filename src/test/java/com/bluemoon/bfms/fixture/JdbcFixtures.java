package com.bluemoon.bfms.fixture;

import static com.bluemoon.bfms.fixture.TestConstants.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.jdbc.core.JdbcTemplate;

public final class JdbcFixtures {
    private final JdbcTemplate jdbc;

    public JdbcFixtures(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public record OccupiedHousehold(long apartmentId, long headPersonId, long householdId) {
    }

    private long insert(String sql, Object... args) {
        Long id = jdbc.queryForObject(sql + " RETURNING id", Long.class, args);
        return java.util.Objects.requireNonNull(id, "INSERT ... RETURNING id trả về null");
    }

    // ---------- persons ----------
    public long person(String fullName, String nationalId, String phone, String email,
            LocalDate dob, String gender) {
        return insert("INSERT INTO persons (full_name, national_id, phone, email, date_of_birth, gender) "
                + "VALUES (?,?,?,?,?,?)", fullName, nationalId, phone, email, dob, gender);
    }

    /** Người hợp lệ đầy đủ: đủ điều kiện làm chủ hộ hoặc chủ tài khoản. */
    public long person() {
        return person("Người Test", uniqueNationalId(), uniquePhone(), uniqueEmail(),
                LocalDate.of(1990, 1, 1), GENDER_MALE);
    }

    /** Giống bé trong seed. Chỉ làm CHILD, không làm chủ hộ hoặc chủ tài khoản. */
    public long personWithoutIdentity() {
        return person("Bé Test", null, null, null, LocalDate.of(2016, 11, 5), GENDER_MALE);
    }

    // ---------- users ----------
    /**
     * Tạo person (đủ email, phone) + user. Trả về users.id. Mật khẩu =
     * Password@123.
     */
    public long account(String username, String role, boolean active) {
        long personId = person();
        return insert("INSERT INTO users (username, password_hash, person_id, role, is_active) "
                + "VALUES (?,?,?,?,?)", username, DEMO_PASSWORD_HASH, personId, role, active);
    }

    public long admin() {
        return account(uniqueUsername(), ROLE_ADMIN, true);
    }

    // ---------- apartments ----------
    public long apartment(String room, int floor, BigDecimal area, String type, String status) {
        return insert("INSERT INTO apartments (room_number, floor_number, area_sqm, apartment_type, status) "
                + "VALUES (?,?,?,?,?)", room, floor, area, type, status);
    }

    public long apartment() {
        return apartment(uniqueRoom(), 5, AREA_TYPICAL, TYPE_RESIDENTIAL, APT_VACANT);
    }

    // ---------- households / residents ----------
    /**
     * Hộ ACTIVE, chưa có dòng residents. DB cho phép; chỉ ràng buộc khi đã có dòng
     * HEAD.
     */
    public long household(long apartmentId, long headPersonId) {
        return insert("INSERT INTO households (apartment_id, head_person_id, registration_code, moved_in_date) "
                + "VALUES (?,?,?,?)", apartmentId, headPersonId, uniqueRegistrationCode(),
                LocalDate.of(2026, 10, 1));
    }

    public long resident(long householdId, long personId, String relationship, String status,
            LocalDate start, LocalDate end) {
        return insert("INSERT INTO residents (person_id, household_id, relationship_with_head, "
                + "residence_status, start_date, end_date) VALUES (?,?,?,?,?,?)",
                personId, householdId, relationship, status, start, end);
    }

    /**
     * Căn OCCUPIED + hộ ACTIVE + chủ hộ THUONG_TRU/HEAD, mô phỏng P.601 trong seed.
     */
    public OccupiedHousehold occupiedHousehold() {
        long head = person();
        long apt = apartment();
        long hh = household(apt, head);
        resident(hh, head, REL_HEAD, RES_THUONG_TRU, null, null);
        jdbc.update("UPDATE apartments SET status = 'OCCUPIED' WHERE id = ?", apt);
        return new OccupiedHousehold(apt, head, hh);
    }

    // ---------- history / audit ----------
    /** previousPersonId = null → lần gán đầu (cả previous_snapshot cũng null). */
    public long headHistory(long householdId, Long previousPersonId, long newPersonId,
            long actorUserId, OffsetDateTime changedAt) {
        String prev = previousPersonId == null ? null : snapshotOf(previousPersonId);
        return insert("INSERT INTO household_head_history (household_id, previous_head_person_id, "
                + "new_head_person_id, previous_snapshot, new_snapshot, changed_at, changed_by_user_id) "
                + "VALUES (?,?,?,?::jsonb,?::jsonb,?,?)",
                householdId, previousPersonId, newPersonId, prev, snapshotOf(newPersonId),
                changedAt, actorUserId);
    }

    public long audit(Long userId, String action, String entityName, Long entityId, String details) {
        return insert("INSERT INTO audit_logs (user_id, action, entity_name, entity_id, details) "
                + "VALUES (?,?,?,?,?)", userId, action, entityName, entityId, details);
    }

    private String snapshotOf(long personId) {
        return jdbc.queryForObject(
                "SELECT full_name, national_id, phone, email FROM persons WHERE id = ?",
                (rs, i) -> snapshotJson(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)),
                personId);
    }

    public long count(String table) {
        Long n = jdbc.queryForObject("SELECT count(*) FROM " + table, Long.class);
        return java.util.Objects.requireNonNull(n);
    }

    /** Lấy SQLSTATE từ exception bị Spring bọc. */
    public static String sqlStateOf(Throwable t) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (c instanceof SQLException s && s.getSQLState() != null)
                return s.getSQLState();
        }
        return null;
    }
}
