package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.Household;
import com.bluemoon.bfms.model.HouseholdStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Truy vấn hộ gia đình, bao gồm hộ hiện hoạt của căn và các hộ do một người đại diện.
 */
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    /**
     * Lấy hộ theo căn/trạng thái và nạp hồ sơ chủ hộ trong cùng truy vấn.
     * Trang chi tiết đọc các trường hồ sơ ngay sau khi gọi phương thức này;
     * EntityGraph tránh phát sinh thêm một SELECT riêng cho chủ hộ.
     *
     * @param apartmentId ID căn hộ
     * @param status trạng thái cần lọc, thường là ACTIVE
     * @return hộ khớp nếu có
     */
    @EntityGraph(attributePaths = "headPerson")
    Optional<Household> findByApartmentIdAndStatus(Long apartmentId, HouseholdStatus status);

    /**
     * Lấy các hộ có cùng chủ hộ theo ID tăng dần ổn định.
     *
     * @param personId ID hồ sơ người đại diện
     * @return danh sách hộ mà người này là chủ hộ
     */
    List<Household> findAllByHeadPersonIdOrderById(Long personId);
}
