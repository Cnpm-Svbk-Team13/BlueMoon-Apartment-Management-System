package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.HeadProfileForm;
import com.bluemoon.bfms.dto.HouseholdHeadForm;
import com.bluemoon.bfms.exception.ApartmentNotFoundException;
import com.bluemoon.bfms.model.*;
import com.bluemoon.bfms.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * Điều phối gán/thay chủ hộ và cập nhật hồ sơ chủ hộ theo BE-03, US-001, RAW-002.
 *
 * <p>Các thao tác ghi thay đổi Household, Person, lịch sử và audit trong cùng
 * transaction để dữ liệu nghiệp vụ và dấu vết thay đổi luôn nhất quán.</p>
 */
@Service
public class HouseholdHeadService {
    private final ApartmentRepository apartments;
    private final PersonRepository persons;
    private final HouseholdRepository households;
    private final ResidentRepository residents;
    private final HouseholdHeadHistoryRepository history;
    private final AuditLogRepository audits;
    private final UserRepository users;
    private final HouseholdWriteLock writeLock;
    private final Validator validator;

    /** Spring tiêm các Repository và Validator cần thiết cho luồng cập nhật chủ hộ. */
    public HouseholdHeadService(ApartmentRepository apartments, PersonRepository persons,
            HouseholdRepository households, ResidentRepository residents,
            HouseholdHeadHistoryRepository history, AuditLogRepository audits,
            UserRepository users, HouseholdWriteLock writeLock, Validator validator) {
        this.apartments = apartments;
        this.persons = persons;
        this.households = households;
        this.residents = residents;
        this.history = history;
        this.audits = audits;
        this.users = users;
        this.writeLock = writeLock;
        this.validator = validator;
    }

    /**
     * Gán chủ hộ lần đầu hoặc thay chủ hộ hiện tại của căn hộ.
     *
     * @param apartmentId ID căn hộ cần cập nhật
     * @param form thông tin chủ hộ và ID chủ hộ hiện tại mà form đã đọc
     * @param actorId ID tài khoản đã xác thực, dùng làm tác nhân trong nhật ký/lịch sử
     * @param ipAddress địa chỉ IP client tùy chọn
     * @throws ApartmentNotFoundException nếu không tìm thấy căn hộ
     * @throws IllegalArgumentException nếu dữ liệu hoặc trạng thái hộ hiện tại không hợp lệ
     */
    // READ_COMMITTED kết hợp write lock để các lần gán/thay đồng thời không cùng sửa một hộ.
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void assignOrReplace(Long apartmentId, HouseholdHeadForm form, Long actorId, String ipAddress) {
        validate(form, actorId, ipAddress);
        // Khóa nghiệp vụ trước khi đọc trạng thái hiện tại; lock được giữ đến khi transaction commit/rollback.
        writeLock.acquire();
        Apartment apartment = apartments.findById(apartmentId).orElseThrow(ApartmentNotFoundException::new);
        Household household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE)
                .orElse(null);
        Person previous = household == null ? null : household.getHeadPerson();
        // So sánh ID từ form với dữ liệu vừa đọc để từ chối yêu cầu gửi từ trang đã lỗi thời.
        checkExpectedHead(form, previous);
        if (household == null && form.getMovedInDate() == null) {
            throw new IllegalArgumentException("Please enter the move-in date.");
        }

        Person next = resolveHead(form);
        if (previous != null && Objects.equals(previous.getId(), next.getId())) {
            throw new IllegalArgumentException("This person is already the household head.");
        }
        Resident nextResidence = residents.findByPersonId(next.getId()).orElse(null);
        // Một người không thể đồng thời là cư dân hiện tại của hai hộ khác nhau.
        if (nextResidence != null && (household == null
                || !Objects.equals(nextResidence.getHousehold().getId(), household.getId()))) {
            throw new IllegalArgumentException("The selected person resides in another household.");
        }

        if (household == null) {
            // Chưa có hộ đang hoạt động: khởi tạo mã hộ và ngày chuyển vào trong cùng transaction.
            household = households.save(new Household(apartment, next,
                    UUID.randomUUID().toString().toUpperCase(Locale.ROOT), form.getMovedInDate()));
        } else {
            // Khi thay chủ hộ, cập nhật quan hệ cư trú của người cũ/mới trước khi đổi FK chủ hộ.
            updateRelationships(household, previous, nextResidence, form.getPreviousHeadRelationship());
            household.changeHead(next);
        }
        recordChange(household, previous, next, previous == null ? null : snapshot(previous),
                actorId, ipAddress, previous == null ? "ASSIGN_HEAD" : "REPLACE_HEAD");
    }

    /**
     * Cập nhật hồ sơ Person dùng chung và ghi snapshot vào lịch sử của mọi hộ có cùng chủ hộ.
     *
     * @param apartmentId ID căn hộ đang hiển thị hồ sơ
     * @param form hồ sơ mới và ID chủ hộ hiện tại mà form đã đọc
     * @param actorId ID tài khoản đã xác thực, dùng làm tác nhân trong nhật ký/lịch sử
     * @param ipAddress địa chỉ IP client tùy chọn
     * @throws IllegalArgumentException nếu dữ liệu hoặc trạng thái hộ hiện tại không hợp lệ
     */
    // Rollback cả thay đổi Person và các bản ghi lịch sử nếu một thao tác ghi phát sinh exception.
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updateProfile(Long apartmentId, HouseholdHeadForm form, Long actorId, String ipAddress) {
        validate(form, actorId, ipAddress);
        writeLock.acquire();
        Household household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("No active household exists."));
        Person person = household.getHeadPerson();
        checkExpectedHead(form, person);
        HeadProfileForm profile = form.getHeadPerson();
        // CCCD là khóa định danh duy nhất; chỉ cho phép giữ CCCD của chính người đang sửa.
        persons.findByNationalId(profile.getNationalId()).ifPresent(existing -> {
            if (!Objects.equals(existing.getId(), person.getId())) {
                throw new IllegalArgumentException("National ID already belongs to another person.");
            }
        });
        JsonNode before = snapshot(person);
        applyProfile(person, profile);
        if (before.equals(snapshot(person))) {
            // Không tạo audit/lịch sử nếu dữ liệu gửi lên không làm thay đổi hồ sơ.
            return;
        }
        // Person được các hộ tham chiếu dùng chung nên phải lưu snapshot mới cho từng hộ liên quan.
        for (Household affected : households.findAllByHeadPersonIdOrderById(person.getId())) {
            recordChange(affected, person, person, before, actorId, ipAddress, "UPDATE_HEAD_PROFILE");
        }
    }

    private void validate(HouseholdHeadForm form, Long actorId, String ipAddress) {
        // Service tự xác minh DTO để các lời gọi không qua Controller cũng không bỏ qua ràng buộc.
        if (form == null || !validator.validate(form).isEmpty()) {
            throw new IllegalArgumentException("Please provide valid head profile details.");
        }
        if (actorId == null || actorId <= 0 || (ipAddress != null && ipAddress.length() > 50)) {
            throw new IllegalArgumentException("Invalid audit actor or client IP address.");
        }
    }

    private void checkExpectedHead(HouseholdHeadForm form, Person currentHead) {
        Long currentId = currentHead == null ? null : currentHead.getId();
        // ID null biểu thị trạng thái chưa có chủ hộ; mọi khác biệt đều cần người dùng tải lại trang.
        if (!Objects.equals(form.getExpectedHeadPersonId(), currentId)) {
            throw new IllegalArgumentException("The household head has changed. Reload the page and try again.");
        }
    }

    private Person resolveHead(HouseholdHeadForm form) {
        HeadProfileForm profile = form.getHeadPerson();
        Person existing = persons.findByNationalId(profile.getNationalId()).orElse(null);
        if (existing != null) {
            // Bắt buộc xác nhận hồ sơ đã tra cứu để tránh vô tình gắn nhầm người chỉ bằng CCCD.
            if (!Objects.equals(existing.getId(), form.getConfirmedPersonId())) {
                throw new IllegalArgumentException("Look up and confirm the existing person before saving.");
            }
            if (existing.getPhone() == null) {
                throw new IllegalArgumentException("The existing person needs a valid phone number.");
            }
            return existing;
        }
        if (form.getConfirmedPersonId() != null) {
            // Hồ sơ được xác nhận có thể đã đổi/mất trong lúc người dùng mở form; không tự tạo bản ghi thay thế.
            throw new IllegalArgumentException("The selected person no longer matches the national ID.");
        }
        return persons.save(new Person(profile.getFullName().strip(), profile.getNationalId(),
                profile.getPhone(), normalizedEmail(profile.getEmail())));
    }

    private void updateRelationships(
            Household household, Person previous, Resident next, RelationshipWithHead replacement) {
        // Nếu chủ hộ cũ đang có hồ sơ cư trú trong hộ này, yêu cầu chọn quan hệ mới thay vì giữ HEAD.
        residents.findByPersonId(previous.getId())
                .filter(old -> Objects.equals(old.getHousehold().getId(), household.getId()))
                .ifPresent(old -> {
                    if (replacement == null || replacement == RelationshipWithHead.HEAD) {
                        throw new IllegalArgumentException("Select a new relationship for the previous head.");
                    }
                    old.changeRelationship(replacement);
                });
        if (next != null) {
            next.changeRelationship(RelationshipWithHead.HEAD);
        }
    }

    private void applyProfile(Person person, HeadProfileForm profile) {
        person.updateHeadProfile(profile.getFullName().strip(), profile.getNationalId(),
                profile.getPhone(), normalizedEmail(profile.getEmail()));
    }

    private String normalizedEmail(String email) {
        return email == null || email.isBlank() ? null : email.strip().toLowerCase(Locale.ROOT);
    }

    private JsonNode snapshot(Person person) {
        // Snapshot lưu đúng các trường hồ sơ cần xem lại; không đưa thông tin xác thực vào lịch sử.
        return JsonNodeFactory.instance.objectNode()
                .put("full_name", person.getFullName())
                .put("national_id", person.getNationalId())
                .put("phone", person.getPhone())
                .put("email", person.getEmail());
    }

    private void recordChange(Household household, Person previous, Person next, JsonNode before,
            Long actorId, String ipAddress, String action) {
        // Dùng cùng tham chiếu User cho lịch sử và nhật ký; getReferenceById tránh tải hồ sơ tài khoản thực hiện.
        User actor = users.getReferenceById(actorId);
        history.save(new HouseholdHeadHistory(household, previous, next, before, snapshot(next), actor));
        audits.save(new AuditLog(actor, action, "Household", household.getId(), null, ipAddress));
    }
}
