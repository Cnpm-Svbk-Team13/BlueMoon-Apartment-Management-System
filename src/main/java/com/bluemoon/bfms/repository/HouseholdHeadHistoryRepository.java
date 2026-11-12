package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.HouseholdHeadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Truy vấn lịch sử chủ hộ theo căn hoặc theo hộ.
 *
 * <p>Các method được Spring Data JPA chuyển thành SELECT có JOIN qua
 * {@code household_id}/{@code apartment_id}; sắp xếp thêm theo ID để thứ tự
 * ổn định khi nhiều sự kiện cùng timestamp transaction.</p>
 */
public interface HouseholdHeadHistoryRepository extends JpaRepository<HouseholdHeadHistory, Long> {
    /**
     * Lấy toàn bộ lịch sử của các hộ từng gắn với một căn hộ.
     *
     * @param apartmentId ID căn hộ
     * @return lịch sử mới nhất trước, dùng ID giảm dần để phá hòa
     */
    List<HouseholdHeadHistory> findByHouseholdApartmentIdOrderByChangedAtDescIdDesc(Long apartmentId);

    /**
     * Lấy lịch sử của một hộ cụ thể mà không cần truy vấn qua mã căn hộ.
     *
     * @param householdId ID hộ gia đình
     * @return các sự kiện từ mới đến cũ
     */
    List<HouseholdHeadHistory> findByHousehold_IdOrderByChangedAtDescIdDesc(Long householdId);

    /**
     * Lấy sự kiện gần nhất của hộ theo thời điểm, rồi theo ID khi timestamp trùng.
     *
     * @param householdId ID hộ gia đình
     * @return sự kiện mới nhất hoặc rỗng nếu hộ chưa có lịch sử
     */
    Optional<HouseholdHeadHistory> findFirstByHousehold_IdOrderByChangedAtDescIdDesc(Long householdId);
}
