package com.bluemoon.bfms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import java.time.OffsetDateTime;

/**
 * Ánh xạ một mặt bằng trong bảng {@code apartments}.
 *
 * <p>Entity này giữ kiểu dữ liệu tương ứng với schema: diện tích dùng
 * {@link BigDecimal}, loại và trạng thái được lưu bằng tên enum, còn thời điểm
 * tạo/cập nhật do database quản lý. Các quy tắc kiểm tra ở đây giúp dữ liệu
 * không hợp lệ bị chặn cả khi entity được tạo ngoài luồng form hiện tại.</p>
 */
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã căn không được để trống.")
    @Size(max = 20, message = "Mã căn không được dài quá 20 ký tự.")
    @Column(name = "room_number", nullable = false, length = 20, unique = true)
    private String roomNumber;

    @NotNull(message = "Tầng không được để trống.")
    @Min(value = 1, message = "Tầng nhỏ nhất là 1.")
    @Max(value = 30, message = "Tầng lớn nhất là 30.")
    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @NotNull(message = "Diện tích không được để trống.")
    @DecimalMin(value = "0.01", message = "Diện tích phải lớn hơn 0.")
    @Column(name = "area_sqm", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaSqm;

    @NotNull(message = "Loại mặt bằng không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type", nullable = false, length = 20)
    private ApartmentType apartmentType;

    @NotNull(message = "Trạng thái căn hộ không được để trống.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApartmentStatus apartmentStatus = ApartmentStatus.VACANT;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    protected Apartment(){
    }

    /**
     * Tạo mặt bằng mới với trạng thái mặc định VACANT.
     *
     * @param roomNumber mã căn duy nhất
     * @param floorNumber tầng từ 1 đến 30
     * @param areaSqm diện tích sử dụng, phải lớn hơn 0
     * @param apartmentType loại mặt bằng theo danh mục của schema
     */
    public Apartment(String roomNumber, Integer floorNumber, BigDecimal areaSqm, ApartmentType apartmentType) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.areaSqm = areaSqm;
        this.apartmentType = apartmentType;
    }

    /** @return khóa chính do database sinh sau khi lưu */
    public Long getId() {
        return id;
    }

    /**
     * Cập nhật các thông tin nghiệp vụ được phép thay đổi của mặt bằng.
     *
     * @param roomNumber mã căn đã chuẩn hóa và còn duy nhất
     * @param floorNumber tầng của mặt bằng
     * @param areaSqm diện tích sử dụng theo đơn vị mét vuông
     * @param apartmentType loại mặt bằng
     * @param status trạng thái do người quản lý xác nhận
     * @param description ghi chú tùy chọn
     */
    public void updateDetails(String roomNumber, Integer floorNumber, BigDecimal areaSqm,
            ApartmentType apartmentType, ApartmentStatus status, String description) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.areaSqm = areaSqm;
        this.apartmentType = apartmentType;
        this.apartmentStatus = status;
        this.description = description;
    }

    /** @return mã căn, không phải khóa chính */
    public String getRoomNumber() {
        return roomNumber;
    }

    /** @return số tầng */
    public Integer getFloorNumber() {
        return floorNumber;
    }

    /** @return diện tích sử dụng dưới dạng số thập phân chính xác */
    public BigDecimal getAreaSqm() {
        return areaSqm;
    }

    /** @return loại mặt bằng */
    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    /** @return trạng thái căn hộ */
    public ApartmentStatus getStatus() {
        return apartmentStatus;
    }

    /** @return ghi chú hoặc {@code null} nếu không có */
    public String getDescription() {
        return description;
    }

    /** @return thời điểm tạo do database ghi nhận */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    /** @return thời điểm cập nhật gần nhất do database ghi nhận */
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
