package com.bluemoon.bfms.service;

import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.AuditLog;
import com.bluemoon.bfms.repository.UserRepository;
import com.bluemoon.bfms.repository.ApartmentRepository;
import com.bluemoon.bfms.repository.AuditLogRepository;
import com.bluemoon.bfms.dto.ApartmentForm;
import com.bluemoon.bfms.exception.DuplicateRoomNumberException;
import com.bluemoon.bfms.exception.ApartmentNotFoundException;
import com.bluemoon.bfms.model.ApartmentStatus;
import org.springframework.data.domain.Sort;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import java.util.Locale;

/** Nghiệp vụ tạo, tra cứu và cập nhật căn hộ theo BE-02 / US-001. */
@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    /** Spring tiêm các Repository lưu căn hộ, nhật ký và tham chiếu tài khoản thực hiện. */
    public ApartmentService(
            ApartmentRepository apartmentRepository,
            AuditLogRepository auditLogRepository,
            UserRepository userRepository) {
        this.apartmentRepository = apartmentRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Tạo căn hộ và bản ghi audit trong cùng một transaction theo BE-02
     * (US-001, RAW-001, RAW-002).
     *
     * @param form dữ liệu căn hộ đã được nhập
     * @param currentUserId ID tài khoản thực hiện lấy từ phiên xác thực phía server
     * @param clientIpAddress IP client tùy chọn, tối đa 50 ký tự
     * @return căn hộ đã được lưu
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ hoặc mã căn đã tồn tại
     * @throws DataIntegrityViolationException nếu vi phạm constraint, kể cả trùng mã do ghi đồng thời
     */
    // BEGIN/COMMIT bao trọn việc lưu căn hộ và audit; rollbackFor cũng rollback với checked exception.
    @Transactional(rollbackFor = Exception.class)
    public Apartment createApartment(ApartmentForm form, Long currentUserId, String clientIpAddress) {
        validateApartmentDetails(form);
        validateActor(currentUserId, clientIpAddress);
        String roomNumber = validateNewRoomNumber(form.getRoomNumber());
        Apartment apartment = new Apartment(
                roomNumber, form.getFloorNumber(), form.getAreaSqm(), form.getApartmentType());
        apartment.updateDetails(roomNumber, form.getFloorNumber(), form.getAreaSqm(),
                form.getApartmentType(), ApartmentStatus.VACANT, normalizedDescription(form));
        return saveWithAudit(apartment, "CREATE", currentUserId, clientIpAddress);
    }

    /**
     * Liệt kê căn hộ theo tầng và mã căn.
     *
     * @param floorNumber tầng cần lọc; null nghĩa là lấy tất cả tầng
     * @return danh sách căn hộ thỏa bộ lọc
     * @throws IllegalArgumentException nếu tầng nằm ngoài khoảng 1 đến 30
     */
    // Đánh dấu transaction chỉ đọc để Spring/JPA xử lý truy vấn theo chế độ không ghi dữ liệu.
    @Transactional(readOnly = true)
    public List<Apartment> listApartments(Integer floorNumber) {
        if (floorNumber == null) {
            return apartmentRepository.findAll(Sort.by("floorNumber", "roomNumber"));
        }
        validateFloorNumber(floorNumber);
        return apartmentRepository.findByFloorNumberOrderByRoomNumber(floorNumber);
    }

    /**
     * Tải dữ liệu hiện có và chuyển thành form chỉnh sửa.
     *
     * @param id ID căn hộ cần sửa
     * @return form đã điền các giá trị đang lưu
     * @throws ApartmentNotFoundException nếu không tìm thấy căn hộ
     */
    @Transactional(readOnly = true)
    public ApartmentForm getApartmentForEdit(Long id) {
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(ApartmentNotFoundException::new);
        ApartmentForm form = new ApartmentForm();
        form.setRoomNumber(apartment.getRoomNumber());
        form.setFloorNumber(apartment.getFloorNumber());
        form.setAreaSqm(apartment.getAreaSqm());
        form.setApartmentType(apartment.getApartmentType());
        form.setStatus(apartment.getStatus());
        form.setDescription(apartment.getDescription());
        return form;
    }

    /**
     * Cập nhật thông tin căn hộ và ghi audit trong cùng transaction.
     *
     * @param id ID căn hộ cần cập nhật
     * @param form các giá trị mới do người dùng gửi
     * @param currentUserId ID tài khoản thực hiện lấy từ phiên xác thực phía server
     * @param clientIpAddress IP client tùy chọn
     * @return căn hộ sau khi cập nhật
     * @throws ApartmentNotFoundException nếu không tìm thấy căn hộ
     * @throws IllegalArgumentException nếu thông tin hoặc tài khoản thực hiện không hợp lệ
     * @throws DataIntegrityViolationException nếu vi phạm constraint lưu trữ
     */
    // Thay đổi trạng thái chỉ được thực hiện qua form cập nhật có chỉ định trạng thái rõ ràng.
    @Transactional(rollbackFor = Exception.class)
    public Apartment updateApartment(
            Long id, ApartmentForm form, Long currentUserId, String clientIpAddress) {
        validateApartmentDetails(form);
        validateActor(currentUserId, clientIpAddress);
        if (form.getStatus() == null) {
            throw new IllegalArgumentException("Please select an apartment status.");
        }
        Apartment apartment = apartmentRepository.findById(id).orElseThrow(ApartmentNotFoundException::new);
        String roomNumber = normalizeRoomNumber(form.getRoomNumber());
        if (!roomNumber.equals(apartment.getRoomNumber()) && apartmentRepository.existsByRoomNumber(roomNumber)) {
            throw new DuplicateRoomNumberException();
        }
        apartment.updateDetails(roomNumber, form.getFloorNumber(), form.getAreaSqm(),
                form.getApartmentType(), form.getStatus(), normalizedDescription(form));
        return saveWithAudit(apartment, "UPDATE", currentUserId, clientIpAddress);
    }

    private void validateActor(Long currentUserId, String clientIpAddress) {
        if (currentUserId == null || currentUserId <= 0) {
            throw new IllegalArgumentException("An authenticated user ID is required.");
        }
        if (clientIpAddress != null && clientIpAddress.length() > 50) {
            throw new IllegalArgumentException("Client IP address must contain at most 50 characters.");
        }

    }

    private String normalizedDescription(ApartmentForm form) {
        String description = form.getDescription();
        // Chuỗi trống chỉ mang ý nghĩa "không có mô tả", nên lưu thành null thay vì giữ khoảng trắng.
        return description == null || description.isBlank() ? null : description.strip();
    }

    private Apartment saveWithAudit(
            Apartment apartment, String action, Long currentUserId, String clientIpAddress) {
        Apartment savedApartment;
        try {
            savedApartment = apartmentRepository.saveAndFlush(apartment);
        } catch (DataIntegrityViolationException exception) {
            // Kiểm tra trước không ngăn được hai transaction cùng tạo mã căn; nhận diện unique constraint từ DB.
            Throwable cause = exception.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException violation
                        && "apartments_room_number_key".equals(violation.getConstraintName())) {
                    throw new DuplicateRoomNumberException();
                }
                cause = cause.getCause();
            }
            throw exception;
        }

        // Dùng proxy theo ID để JPA ghi khóa ngoại user_id mà không tải toàn bộ hồ sơ User.
        AuditLog auditLog = new AuditLog(
                userRepository.getReferenceById(currentUserId), action, "Apartment", savedApartment.getId(),
                action.equals("CREATE") ? "Created apartment " + savedApartment.getRoomNumber()
                        : "Updated apartment " + savedApartment.getRoomNumber(),
                clientIpAddress);
        auditLogRepository.saveAndFlush(auditLog);
        return savedApartment;
    }

    /**
     * Tra cứu căn hộ theo mã căn.
     *
     * @param roomNumber mã căn cần tìm
     * @return căn hộ có mã khớp chính xác, hoặc Optional rỗng nếu chưa có
     */
    @Transactional(readOnly = true)
    public Optional<Apartment> findByRoomNumber(String roomNumber) {
        return apartmentRepository.findByRoomNumber(normalizeRoomNumber(roomNumber));
    }

    /**
     * Kiểm tra mã căn đã tồn tại hay chưa.
     *
     * @param roomNumber mã căn cần kiểm tra
     * @return true nếu mã căn đã được sử dụng
     */
    @Transactional(readOnly = true)
    public boolean existsByRoomNumber(String roomNumber) {
        return apartmentRepository.existsByRoomNumber(normalizeRoomNumber(roomNumber));
    }

    /**
     * Lấy các căn hộ thuộc một tầng cụ thể.
     *
     * @param floorNumber số tầng cần tra cứu
     * @return danh sách căn hộ thuộc tầng đó
     */
    @Transactional(readOnly = true)
    public List<Apartment> findByFloorNumber(Integer floorNumber) {
        validateFloorNumber(floorNumber);
        return apartmentRepository.findByFloorNumberOrderByRoomNumber(floorNumber);
    }

    private void validateFloorNumber(Integer floorNumber) {
        if (floorNumber == null || floorNumber < 1 || floorNumber > 30) {
            throw new IllegalArgumentException("Floor number must be between 1 and 30.");
        }
    }

    private String validateNewRoomNumber(String roomNumber) {
        String normalizedRoomNumber = normalizeRoomNumber(roomNumber);
        // Kiểm tra sớm để trả lỗi thân thiện; unique constraint vẫn là chốt cuối khi có ghi đồng thời.
        if (apartmentRepository.existsByRoomNumber(normalizedRoomNumber)) {
            throw new DuplicateRoomNumberException();
        }
        return normalizedRoomNumber;
    }

    private String normalizeRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Please enter a room number.");
        }

        // Mã căn lưu dạng uppercase để tìm kiếm/so sánh không phụ thuộc cách nhập hoa-thường.
        String normalizedRoomNumber = roomNumber.trim().toUpperCase(Locale.ROOT);

        if (normalizedRoomNumber.length() > 20) {
            throw new IllegalArgumentException("Room number must contain at most 20 characters.");
        }

        return normalizedRoomNumber;
    }

    private void validateApartmentDetails(ApartmentForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Missing apartment information.");
        }

        Integer floorNumber = form.getFloorNumber();

        if (floorNumber == null || floorNumber < 1 || floorNumber > 30) {
            throw new IllegalArgumentException("Floor Number must be between 1 and 30.");
        }

        BigDecimal areaSqm = form.getAreaSqm();
        // Diện tích thông thủy phải dương, tối đa hai chữ số thập phân và vừa DECIMAL(12,2).
        if (areaSqm == null || areaSqm.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Area Sqm must be larger than 0.");
        }

        if (areaSqm.scale() > 2) {
            throw new IllegalArgumentException("Usable area must have at most two decimal places.");
        }

        if (areaSqm.compareTo(new BigDecimal("9999999999.99")) > 0) {
            throw new IllegalArgumentException(
                    "Usable area exceeds the supported limit."
            );
        }

        ApartmentType apartmentType = form.getApartmentType();
        if (apartmentType == null){
            throw new IllegalArgumentException("Please select apartment type!");
        }
    }
}
