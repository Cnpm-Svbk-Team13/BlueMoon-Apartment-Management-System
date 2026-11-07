package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Household;
import com.bluemoon.bfms.model.HouseholdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Truy vấn hộ gia đình. Các truy vấn đọc danh sách tải căn hộ và chủ hộ cùng
 * lượt, tránh N+1 khi giao diện hiển thị thông tin liên kết.
 */
public interface HouseholdRepository extends JpaRepository<Household, Long> {

    @Override
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    Optional<Household> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    List<Household> findAll();

    @Override
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    Page<Household> findAll(Pageable pageable);

    /**
     * Tìm hộ theo mã đăng ký duy nhất, đồng thời nạp căn hộ và chủ hộ.
     *
     * Tham số registrationCode: mã đăng ký chuẩn hóa
     * Kết quả: hộ nếu tồn tại
     */
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    Optional<Household> findByRegistrationCode(String registrationCode);

    /**
     * Liệt kê các hộ thuộc căn hộ xác định, kèm căn hộ và chủ hộ.
     *
     * Tham số apartmentId: ID căn hộ
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang hộ thuộc căn hộ
     */
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    Page<Household> findByApartment_Id(Long apartmentId, Pageable pageable);

    /**
     * Liệt kê hộ theo trạng thái, kèm dữ liệu liên kết thường được hiển thị.
     *
     * Tham số status: trạng thái hộ
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang hộ có trạng thái tương ứng
     */
    @EntityGraph(attributePaths = {"apartment", "headPerson"})
    Page<Household> findByStatus(HouseholdStatus status, Pageable pageable);
}
