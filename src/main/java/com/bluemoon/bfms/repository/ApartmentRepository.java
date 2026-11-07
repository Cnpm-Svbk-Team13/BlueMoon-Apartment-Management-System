package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Apartment;
import com.bluemoon.bfms.model.ApartmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Truy vấn danh mục căn hộ. */
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    /**
     * Tìm căn hộ theo mã phòng duy nhất.
     *
     * Tham số roomNumber: mã phòng chuẩn hóa
     * Kết quả: căn hộ nếu tồn tại
     */
    Optional<Apartment> findByRoomNumber(String roomNumber);

    /**
     * Liệt kê căn hộ theo tầng với phân trang.
     *
     * Tham số floorNumber: số tầng
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang các căn hộ thuộc tầng được chọn
     */
    Page<Apartment> findByFloorNumber(int floorNumber, Pageable pageable);

    /**
     * Liệt kê căn hộ theo trạng thái với phân trang.
     *
     * Tham số status: trạng thái cần lọc
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang căn hộ có trạng thái tương ứng
     */
    Page<Apartment> findByStatus(ApartmentStatus status, Pageable pageable);
}
