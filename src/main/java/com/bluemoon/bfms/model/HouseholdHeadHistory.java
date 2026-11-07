package com.bluemoon.bfms.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Immutable;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;

/** JPA entity chỉ-ghi-thêm cho lịch sử thay đổi chủ hộ; snapshot được lưu bằng PostgreSQL JSONB. */
@Entity
@Immutable
@Table(name = "household_head_history")
public class HouseholdHeadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_head_person_id")
    private Person previousHeadPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "new_head_person_id", nullable = false)
    private Person newHeadPerson;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_snapshot", columnDefinition = "jsonb")
    private JsonNode previousSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_snapshot", nullable = false, columnDefinition = "jsonb")
    private JsonNode newSnapshot;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "changed_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private User changedByUser;

    protected HouseholdHeadHistory() {
    }

    public HouseholdHeadHistory(
            Long id,
            Household household,
            Person previousHeadPerson,
            Person newHeadPerson,
            JsonNode previousSnapshot,
            JsonNode newSnapshot,
            OffsetDateTime changedAt,
            User changedByUser) {
        this.id = id;
        this.household = household;
        this.previousHeadPerson = previousHeadPerson;
        this.newHeadPerson = newHeadPerson;
        this.previousSnapshot = previousSnapshot;
        this.newSnapshot = newSnapshot;
        this.changedAt = changedAt;
        this.changedByUser = changedByUser;
    }

    public Long id() { return id; }
    public Household household() { return household; }
    public Long householdId() { return household == null ? null : household.id(); }
    public Person previousHeadPerson() { return previousHeadPerson; }
    public Long previousHeadPersonId() {
        return previousHeadPerson == null ? null : previousHeadPerson.id();
    }
    public Person newHeadPerson() { return newHeadPerson; }
    public Long newHeadPersonId() { return newHeadPerson == null ? null : newHeadPerson.id(); }
    public JsonNode previousSnapshot() { return previousSnapshot; }
    public JsonNode newSnapshot() { return newSnapshot; }
    public OffsetDateTime changedAt() { return changedAt; }
    public User changedByUser() { return changedByUser; }
    public Long changedByUserId() { return changedByUser == null ? null : changedByUser.id(); }

    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");

    /**
     * Kiểm tra cấu trúc sự kiện và dữ liệu snapshot chủ hộ.
     *
     * <p>Các ID hộ, chủ hộ mới và tài khoản thực hiện phải dương. ID chủ hộ
     * trước đó và snapshot cũ phải cùng có hoặc cùng vắng mặt; snapshot mới
     * bắt buộc. Mỗi snapshot phải là JSON object có họ tên, CCCD, điện thoại
     * và email đúng định dạng. Việc snapshot có thực sự lấy từ hồ sơ đáng tin
     * cậy hay tài khoản thực hiện có được xác thực không phải được bảo đảm ở
     * tầng service.</p>
     *
     * Có thể ném IllegalArgumentException nếu sự kiện thiếu trường hoặc snapshot không hợp lệ
     */
    public void validate() {
        if (household == null || household.id() == null || household.id() <= 0
                || newHeadPerson == null || newHeadPerson.id() == null || newHeadPerson.id() <= 0
                || changedByUser == null || changedByUser.id() == null || changedByUser.id() <= 0) {
            throw new IllegalArgumentException(
                    "householdId, newHeadPersonId, and changedByUserId must be positive");
        }
        if ((previousHeadPerson == null) != (previousSnapshot == null)) {
            throw new IllegalArgumentException(
                    "previousHeadPersonId and previousSnapshot must both be present or both be null");
        }
        if (previousHeadPerson != null
                && (previousHeadPerson.id() == null || previousHeadPerson.id() <= 0)) {
            throw new IllegalArgumentException("previousHeadPersonId must be positive");
        }
        validateSnapshot(newSnapshot, "newSnapshot");
        if (previousSnapshot != null) {
            validateSnapshot(previousSnapshot, "previousSnapshot");
        }
    }

    /**
     * Kiểm tra đây có phải sự kiện gán chủ hộ lần đầu hay không.
     *
     * Kết quả: true nếu chưa có ID chủ hộ trước đó
     */
    public boolean isInitialAssignment() {
        return previousHeadPerson == null;
    }

    /**
     * Kiểm tra sự kiện có thay chủ hộ bằng một người khác hay không.
     *
     * Kết quả: true nếu có chủ hộ trước đó và ID cũ khác ID mới
     */
    public boolean isHeadReplacement() {
        return previousHeadPerson != null && newHeadPerson != null
                && previousHeadPerson.id() != null && newHeadPerson.id() != null
                && !previousHeadPerson.id().equals(newHeadPerson.id());
    }

    /**
     * Kiểm tra sự kiện có ghi nhận cập nhật hồ sơ của cùng một chủ hộ hay không.
     *
     * Kết quả: true nếu ID chủ hộ trước và sau giống nhau
     */
    public boolean isHeadProfileUpdate() {
        return previousHeadPerson != null && newHeadPerson != null
                && previousHeadPerson.id() != null && newHeadPerson.id() != null
                && previousHeadPerson.id().equals(newHeadPerson.id());
    }

    /**
     * Kiểm tra các trường bắt buộc và định dạng hồ sơ trong một snapshot JSON.
     *
     * Tham số snapshot: snapshot cần kiểm tra
     * Tham số field: tên trường snapshot dùng trong thông báo lỗi
     * Có thể ném IllegalArgumentException nếu JSON không phải object hoặc thông tin hồ sơ không hợp lệ
     */
    private static void validateSnapshot(JsonNode snapshot, String field) {
        if (snapshot == null || !snapshot.isObject()
                || !snapshot.hasNonNull("full_name")
                || !snapshot.path("full_name").isTextual()
                || !snapshot.hasNonNull("national_id")
                || !snapshot.path("national_id").isTextual()
                || !snapshot.hasNonNull("phone")
                || !snapshot.path("phone").isTextual()
                || !snapshot.has("email")) {
            throw new IllegalArgumentException(field + " must contain the required profile fields");
        }
        String fullName = snapshot.path("full_name").asText();
        String nationalId = snapshot.path("national_id").asText();
        String phone = snapshot.path("phone").asText();
        JsonNode emailNode = snapshot.get("email");
        if (fullName.isBlank() || !fullName.equals(fullName.trim()) || fullName.length() > 100
                || !NATIONAL_ID_PATTERN.matcher(nationalId).matches()
                || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(field + " contains an invalid profile value");
        }
        if (!emailNode.isNull()) {
            String email = emailNode.isTextual() ? emailNode.asText() : null;
            if (email == null || email.isBlank() || email.length() > 255
                    || !email.equals(email.trim().toLowerCase(java.util.Locale.ROOT))
                    || !EMAIL_PATTERN.matcher(email).matches()) {
                throw new IllegalArgumentException(field + " contains an invalid email");
            }
        }
    }
}
