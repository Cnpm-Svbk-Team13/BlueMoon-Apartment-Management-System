package com.bluemoon.bfms.dto;

import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.ApartmentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO nhận dữ liệu từ form tạo hoặc chỉnh sửa căn hộ.
 *
 * <p>DTO tách hợp đồng HTTP khỏi Entity JPA. Các annotation kiểm tra dữ liệu
 * đầu vào ở tầng web; Service vẫn chịu trách nhiệm áp dụng quy tắc nghiệp vụ
 * và lưu thay đổi.</p>
 */
public class ApartmentForm {

    /** Mã định danh căn, bắt buộc và tối đa 20 ký tự. */
    @NotBlank(message = "Please enter the room number.")
    @Size(max = 20, message = "Room number must contain at most 20 characters.")
    private String roomNumber;

    /** Tầng của căn; giới hạn theo tòa nhà từ tầng 1 đến tầng 30. */
    @NotNull(message = "Please enter floor number.")
    @Min(value = 1, message = "Floor number must be between 1 and 30!")
    @Max(value = 30, message = "Floor number must be between 1 and 30!")
    private Integer floorNumber;

    /**
     * Diện tích sử dụng (m²), dùng BigDecimal để tránh sai số số thực.
     *
     * <p>Giá trị phải lớn hơn 0 và có tối đa 10 chữ số phần nguyên,
     * 2 chữ số thập phân, tương ứng kiểu dữ liệu tiền/diện tích trong schema.</p>
     */
    @NotNull(message = "Please enter the usable area.")
    @DecimalMin(value = "0.01", message = "Usable area must be greater than zero.")
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Maximum capacity of 10 digits for the integer part and 2 decimal places."
    )
    private BigDecimal areaSqm;

    /** Loại hình mặt bằng lấy từ danh mục ApartmentType, không nhận chuỗi tự do. */
    @NotNull(message = "Please select apartment type!")
    private ApartmentType apartmentType;

    /** Trạng thái khai thác; mặc định là căn đang trống khi tạo mới. */
    private ApartmentStatus status = ApartmentStatus.VACANT;

    /** Ghi chú bổ sung tùy chọn về căn hộ. */
    private String description;

    public ApartmentStatus getStatus() {
        return status;
    }

    public void setStatus(ApartmentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoomNumber(){
        return this.roomNumber;
    }

    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public Integer getFloorNumber(){
        return this.floorNumber;
    }

    public void setFloorNumber(Integer floorNumber){
        this.floorNumber = floorNumber;
    }

    public BigDecimal getAreaSqm(){
        return this.areaSqm;
    }

    public void setAreaSqm(BigDecimal areaSqm){
        this.areaSqm = areaSqm;
    }

    public ApartmentType getApartmentType(){
        return this.apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType){
        this.apartmentType = apartmentType;
    }
}
