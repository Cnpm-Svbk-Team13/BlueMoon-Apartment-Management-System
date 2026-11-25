package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * Tầng truy cập dữ liệu căn hộ.
 *
 * <p>Spring Data JPA tạo truy vấn từ tên phương thức, tương tự các câu
 * {@code SELECT} có điều kiện. Repository chỉ truy vấn/lưu Entity; quy tắc
 * nghiệp vụ và kiểm tra quyền được giữ ở Service.</p>
 */
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    /**
     * Tìm căn hộ theo mã căn.
     *
     * <p>Truy vấn tương đương: {@code SELECT a FROM Apartment a WHERE a.roomNumber = ?1}.</p>
     *
     * @param roomNumber mã căn cần tìm
     * @return căn hộ nếu có mã tương ứng
     */
    Optional<Apartment> findByRoomNumber(String roomNumber);

    /**
     * Kiểm tra mã căn đã được sử dụng hay chưa.
     *
     * <p>Truy vấn tương đương: {@code SELECT COUNT(a) FROM Apartment a WHERE a.roomNumber = ?1};
     * kết quả được chuyển thành {@code true} nếu số lượng lớn hơn 0.</p>
     *
     * @param roomNumber mã căn cần kiểm tra
     * @return {@code true} nếu đã tồn tại, ngược lại {@code false}
     */
    boolean existsByRoomNumber(String roomNumber);

    /**
     * Lấy các căn thuộc một tầng.
     *
     * <p>Truy vấn tương đương: {@code SELECT a FROM Apartment a WHERE a.floorNumber = ?1}.
     * Phương thức này không quy định thứ tự kết quả.</p>
     *
     * @param floorNumber số tầng cần lọc
     * @return danh sách căn tại tầng đó
     */
    List<Apartment> findByFloorNumber(Integer floorNumber);

    /**
     * Lấy các căn thuộc một tầng và sắp xếp theo mã căn tăng dần.
     *
     * <p>Truy vấn tương đương:
     * {@code SELECT a FROM Apartment a WHERE a.floorNumber = ?1 ORDER BY a.roomNumber}.</p>
     *
     * @param floorNumber số tầng cần lọc
     * @return danh sách căn đã sắp xếp theo mã căn
     */
    List<Apartment> findByFloorNumberOrderByRoomNumber(Integer floorNumber);
}
