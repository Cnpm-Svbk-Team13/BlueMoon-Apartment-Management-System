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


@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public ApartmentService(
            ApartmentRepository apartmentRepository,
            AuditLogRepository auditLogRepository,
            UserRepository userRepository) {
        this.apartmentRepository = apartmentRepository;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates an apartment and its audit record atomically for BE-02 (US-001,
     * RAW-001 and RAW-002).
     *
     * @param form apartment details
     * @param currentUserId authenticated actor ID supplied by the server
     * @param clientIpAddress optional client IP address, at most 50 characters
     * @return the persisted apartment
     * @throws IllegalArgumentException if the input is invalid or the room number is taken
     * @throws org.springframework.dao.DataIntegrityViolationException if a database
     *         constraint fails, including a concurrent duplicate room number
     */
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
     * Lists apartments in floor and room order.
     * @param floorNumber optional floor filter
     * @return matching apartments
     * @throws IllegalArgumentException if the floor is outside 1 to 30
     */
    @Transactional(readOnly = true)
    public List<Apartment> listApartments(Integer floorNumber) {
        if (floorNumber == null) {
            return apartmentRepository.findAll(Sort.by("floorNumber", "roomNumber"));
        }
        if (floorNumber < 1 || floorNumber > 30) {
            throw new IllegalArgumentException("Floor number must be between 1 and 30.");
        }
        return apartmentRepository.findByFloorNumberOrderByRoomNumber(floorNumber);
    }

    /**
     * Loads the editable values of an apartment.
     * @param id apartment ID
     * @return populated apartment form
     * @throws ApartmentNotFoundException if the apartment does not exist
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
     * Updates an apartment and writes its audit record in one transaction.
     * @param id apartment ID
     * @param form proposed changes
     * @param currentUserId authenticated actor ID
     * @param clientIpAddress optional client IP address
     * @return the updated apartment
     * @throws ApartmentNotFoundException if the apartment does not exist
     * @throws IllegalArgumentException if the changes or actor are invalid
     * @throws DataIntegrityViolationException if a storage constraint fails
     */
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
        return description == null || description.isBlank() ? null : description.strip();
    }

    private Apartment saveWithAudit(
            Apartment apartment, String action, Long currentUserId, String clientIpAddress) {
        Apartment savedApartment;
        try {
            savedApartment = apartmentRepository.saveAndFlush(apartment);
        } catch (DataIntegrityViolationException exception) {
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

        // Dùng proxy theo ID để JPA ghi khóa ngoại user_id mà không tải cả hồ sơ.
        AuditLog auditLog = new AuditLog(
                userRepository.getReferenceById(currentUserId), action, "Apartment", savedApartment.getId(),
                action.equals("CREATE") ? "Created apartment " + savedApartment.getRoomNumber()
                        : "Updated apartment " + savedApartment.getRoomNumber(),
                clientIpAddress);
        auditLogRepository.saveAndFlush(auditLog);
        return savedApartment;
    }

    @Transactional(readOnly = true)
    public Optional<Apartment> findByRoomNumber(String roomNumber){
        return apartmentRepository.findByRoomNumber(roomNumber);
    } 

    @Transactional(readOnly = true)
    public boolean existsByRoomNumber(String roomNumber){
        return apartmentRepository.existsByRoomNumber(roomNumber);
    }

    @Transactional(readOnly = true)
    public List<Apartment> findByFloorNumber(Integer floorNumber){
        return apartmentRepository.findByFloorNumber(floorNumber);
    }

    private String validateNewRoomNumber(String roomNumber) {
        String normalizedRoomNumber = normalizeRoomNumber(roomNumber);
        if (apartmentRepository.existsByRoomNumber(normalizedRoomNumber)) {
            throw new DuplicateRoomNumberException();
        }
        return normalizedRoomNumber;
    }

    private String normalizeRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("Please enter a room number.");
        }

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
