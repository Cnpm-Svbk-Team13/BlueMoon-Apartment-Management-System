package com.bluemoon.bfms.service;

import com.bluemoon.bfms.dto.ApartmentHouseholdView;
import com.bluemoon.bfms.dto.HeadProfileForm;
import com.bluemoon.bfms.exception.ApartmentNotFoundException;
import com.bluemoon.bfms.model.HouseholdStatus;
import com.bluemoon.bfms.model.Person;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.HouseholdHeadHistoryRepository;
import com.bluemoon.bfms.repository.HouseholdRepository;
import com.bluemoon.bfms.repository.PersonRepository;
import com.bluemoon.bfms.repository.ResidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Đọc hồ sơ chủ hộ và lịch sử thay đổi theo BE-03, US-001, RAW-002.
 *
 * <p>Service chuyển Entity thành read model/form để tầng web không phải truy cập
 * quan hệ JPA hoặc tự ghép lịch sử.</p>
 */
@Service
public class HouseholdDetailsService {
    private final ApartmentRepository apartments;
    private final HouseholdRepository households;
    private final HouseholdHeadHistoryRepository history;
    private final ResidentRepository residents;
    private final PersonRepository persons;

    /** Spring tiêm các Repository phục vụ truy vấn căn hộ, hộ, cư dân và lịch sử. */
    public HouseholdDetailsService(ApartmentRepository apartments, HouseholdRepository households,
            HouseholdHeadHistoryRepository history, ResidentRepository residents, PersonRepository persons) {
        this.apartments = apartments;
        this.households = households;
        this.history = history;
        this.residents = residents;
        this.persons = persons;
    }

    /**
     * Tạo read model cho căn hộ, chủ hộ hiện tại và lịch sử snapshot.
     *
     * @param apartmentId ID căn hộ cần tra cứu
     * @return thông tin căn hộ, hồ sơ chủ hộ nếu có và các sự kiện lịch sử mới trước
     * @throws ApartmentNotFoundException nếu không tìm thấy căn hộ
     */
    // Chỉ đọc dữ liệu; các quan hệ lazy cần dùng được truy cập trước khi transaction đóng.
    @Transactional(readOnly = true)
    public ApartmentHouseholdView getDetails(Long apartmentId) {
        if (apartmentId == null) {
            throw new IllegalArgumentException("Apartment ID must not be null.");
        }
        var apartment = apartments.findById(apartmentId).orElseThrow(ApartmentNotFoundException::new);
        var household = households.findByApartmentIdAndStatus(apartmentId, HouseholdStatus.ACTIVE).orElse(null);
        Person head = household == null ? null : household.getHeadPerson();
        // Cờ này phân biệt người đang đứng tên chủ hộ với người có hồ sơ cư trú trong chính hộ đó.
        boolean headResidesHere = head != null && residents.findByPersonId(head.getId())
                .filter(resident -> resident.getHousehold().getId().equals(household.getId())).isPresent();
        var entries = history.findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(apartmentId).stream()
                .map(entry -> new ApartmentHouseholdView.HistoryEntry(entry.getEventType(),
                        entry.getPreviousSnapshot(), entry.getNewSnapshot(),
                        entry.getChangedAt(), entry.getChangedByUserId()))
                .toList();
        return new ApartmentHouseholdView(apartment.getId(), apartment.getRoomNumber(),
                head == null ? null : head.getId(), head == null ? null : toForm(head), headResidesHere, entries);
    }

    /**
     * Tra hồ sơ Person theo CCCD để người có quyền chọn hồ sơ đã tồn tại.
     *
     * @param nationalId CCCD gồm đúng 12 chữ số cần tra cứu
     * @return ID và form hồ sơ nếu tìm thấy; Optional rỗng nếu chưa có người khớp
     * @throws IllegalArgumentException nếu CCCD sai định dạng
     */
    @Transactional(readOnly = true)
    public Optional<ExistingPerson> findExistingPerson(String nationalId) {
        if (nationalId == null || !nationalId.matches("[0-9]{12}")) {
            throw new IllegalArgumentException("National ID must contain 12 digits.");
        }
        return persons.findByNationalId(nationalId).map(person -> new ExistingPerson(person.getId(), toForm(person)));
    }

    private HeadProfileForm toForm(Person person) {
        HeadProfileForm form = new HeadProfileForm();
        form.setFullName(person.getFullName());
        form.setNationalId(person.getNationalId());
        form.setPhone(person.getPhone());
        form.setEmail(person.getEmail());
        return form;
    }

    /** Kết quả tra cứu tối giản; không trả trực tiếp Entity ra tầng Controller. */
    public record ExistingPerson(Long id, HeadProfileForm profile) {
    }
}
