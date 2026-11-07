package com.bluemoon.bfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Locale;

/** JPA entity căn hộ, ánh xạ bảng apartments và giữ diện tích bằng BigDecimal. */
@Entity
@Table(name = "apartments", uniqueConstraints = {
        @UniqueConstraint(name = "apartments_room_number_key", columnNames = "room_number")
})
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "area_sqm", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaSqm;

    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type", nullable = false, length = 20)
    private ApartmentType apartmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApartmentStatus status = ApartmentStatus.VACANT;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @org.hibernate.annotations.Generated(event = org.hibernate.generator.EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @org.hibernate.annotations.Generated(event = {
            org.hibernate.generator.EventType.INSERT,
            org.hibernate.generator.EventType.UPDATE
    })
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Apartment() {
    }

    public Apartment(
            Long id,
            String roomNumber,
            int floorNumber,
            BigDecimal areaSqm,
            ApartmentType apartmentType,
            ApartmentStatus status,
            String description,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.areaSqm = areaSqm;
        this.apartmentType = apartmentType;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long id() { return id; }
    public String roomNumber() { return roomNumber; }
    public int floorNumber() { return floorNumber; }
    public BigDecimal areaSqm() { return areaSqm; }
    public ApartmentType apartmentType() { return apartmentType; }
    public ApartmentStatus status() { return status; }
    public String description() { return description; }
    public OffsetDateTime createdAt() { return createdAt; }
    public OffsetDateTime updatedAt() { return updatedAt; }

    /**
     * Kiểm tra các ràng buộc căn hộ có thể xác minh từ riêng đối tượng này.
     *
     * <p>Mã căn hộ phải viết hoa, đã trim và tối đa 20 ký tự; tầng nằm trong
     * khoảng 1–30; diện tích sử dụng phải dương, tối đa hai chữ số thập phân
     * và phù hợp DECIMAL(12,2); loại căn và trạng thái phải được gán.
     * Phương thức không kiểm tra mã căn có trùng hay căn có hộ đang hoạt động
     * trong cơ sở dữ liệu hay không.</p>
     *
     * Có thể ném IllegalArgumentException nếu dữ liệu vi phạm một trong các điều kiện trên
     */
    public void validate() {
        if (roomNumber == null || roomNumber.isBlank() || roomNumber.length() > 20
                || !roomNumber.equals(roomNumber.trim().toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("roomNumber must be trimmed, uppercase, and at most 20 characters");
        }
        if (floorNumber < 1 || floorNumber > 30) {
            throw new IllegalArgumentException("floorNumber must be between 1 and 30");
        }
        if (areaSqm == null || areaSqm.signum() <= 0) {
            throw new IllegalArgumentException("areaSqm must be positive");
        }
        try {
            if (areaSqm.setScale(2, RoundingMode.UNNECESSARY).precision() > 12) {
                throw new IllegalArgumentException("areaSqm exceeds DECIMAL(12,2)");
            }
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("areaSqm must have at most two decimal places", exception);
        }
        if (apartmentType == null || status == null) {
            throw new IllegalArgumentException("apartmentType and status are required");
        }
    }

    /**
     * Tạo một bản sao có mã căn, diện tích và mô tả ở dạng chuẩn hóa.
     *
     * <p>Mã căn được trim và viết hoa; mô tả tùy chọn được trim và đổi thành
     * null nếu rỗng; diện tích được đặt scale 2 mà không làm tròn.
     * Nếu diện tích có phần thập phân không thể biểu diễn chính xác ở hai chữ
     * số, phép chuẩn hóa sẽ ném lỗi thay vì âm thầm làm thay đổi dữ liệu.</p>
     *
     * Kết quả: bản sao đã chuẩn hóa
     * Có thể ném ArithmeticException nếu diện tích cần làm tròn để đạt scale 2
     */
    public Apartment normalized() {
        String normalizedRoom = roomNumber == null ? null : roomNumber.trim().toUpperCase(Locale.ROOT);
        BigDecimal normalizedArea = areaSqm == null ? null : areaSqm.setScale(2, RoundingMode.UNNECESSARY);
        String normalizedDescription = description == null || description.isBlank()
                ? null
                : description.trim();
        return new Apartment(id, normalizedRoom, floorNumber, normalizedArea, apartmentType,
                status, normalizedDescription, createdAt, updatedAt);
    }

    /**
     * Kiểm tra trạng thái căn hộ hiện tại có phải là còn trống hay không.
     *
     * Kết quả: true nếu trạng thái là ApartmentStatus.VACANT; false nếu khác hoặc chưa được gán
     */
    public boolean isVacant() {
        return status == ApartmentStatus.VACANT;
    }

    /**
     * Tạo bản sao căn hộ với trạng thái sử dụng mới.
     *
     * <p>Phương thức chỉ cập nhật trạng thái trên đối tượng; việc chuyển trạng
     * thái có phù hợp với các hộ đang liên kết hay không cần được service và
     * cơ sở dữ liệu kiểm tra.</p>
     *
     * Tham số newStatus: trạng thái mới, không được null
     * Kết quả: bản sao căn hộ có trạng thái mới
     * Có thể ném IllegalArgumentException nếu newStatus là null
     */
    public Apartment withStatus(ApartmentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("status is required");
        }
        return new Apartment(id, roomNumber, floorNumber, areaSqm, apartmentType,
                newStatus, description, createdAt, updatedAt);
    }
}
