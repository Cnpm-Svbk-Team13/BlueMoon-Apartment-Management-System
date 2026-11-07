package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.ResidenceStatus;
import com.bluemoon.bfms.model.Resident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Truy vấn hồ sơ cư trú. Tải các quan hệ to-one cần cho màn hình cùng truy vấn
 * để không chạy thêm SELECT cho từng người/hộ.
 */
public interface ResidentRepository extends JpaRepository<Resident, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "person", "household", "household.apartment", "household.headPerson"
    })
    Optional<Resident> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {
            "person", "household", "household.apartment", "household.headPerson"
    })
    List<Resident> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "person", "household", "household.apartment", "household.headPerson"
    })
    Page<Resident> findAll(Pageable pageable);

    /**
     * Tìm hồ sơ cư trú duy nhất của một người và tải liên kết cần thiết.
     *
     * Tham số personId: ID cá nhân
     * Kết quả: hồ sơ cư trú nếu tồn tại
     */
    @EntityGraph(attributePaths = {
            "person", "household", "household.apartment", "household.headPerson"
    })
    Optional<Resident> findByPerson_Id(Long personId);

    /**
     * Liệt kê nhân khẩu của hộ, kèm hồ sơ cá nhân, căn hộ và chủ hộ.
     *
     * Tham số householdId: ID hộ gia đình
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang nhân khẩu trong hộ
     */
    @EntityGraph(attributePaths = {
            "person", "household", "household.apartment", "household.headPerson"
    })
    Page<Resident> findByHousehold_Id(Long householdId, Pageable pageable);

    /**
     * Liệt kê hồ sơ cư trú theo loại trạng thái.
     *
     * Tham số residenceStatus: trạng thái cư trú cần lọc
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang hồ sơ cư trú tương ứng
     */
    @EntityGraph(attributePaths = {"person", "household"})
    Page<Resident> findByResidenceStatus(ResidenceStatus residenceStatus, Pageable pageable);
}
