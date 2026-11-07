package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.HouseholdHeadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Truy vấn lịch sử chủ hộ theo hướng chỉ đọc; entity được đánh dấu immutable. */
public interface HouseholdHeadHistoryRepository extends JpaRepository<HouseholdHeadHistory, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "household", "household.apartment", "previousHeadPerson", "newHeadPerson",
            "changedByUser", "changedByUser.person"
    })
    Optional<HouseholdHeadHistory> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {
            "household", "household.apartment", "previousHeadPerson", "newHeadPerson",
            "changedByUser", "changedByUser.person"
    })
    List<HouseholdHeadHistory> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "household", "household.apartment", "previousHeadPerson", "newHeadPerson",
            "changedByUser", "changedByUser.person"
    })
    Page<HouseholdHeadHistory> findAll(Pageable pageable);

    /**
     * Lấy lịch sử của một hộ theo thứ tự mới nhất trước, nạp các quan hệ hiển thị
     * trong cùng lượt để tránh N+1 khi trình bày lịch sử.
     *
     * Tham số householdId: ID hộ gia đình
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang sự kiện lịch sử, mới nhất trước
     */
    @EntityGraph(attributePaths = {
            "household", "household.apartment", "previousHeadPerson", "newHeadPerson",
            "changedByUser", "changedByUser.person"
    })
    Page<HouseholdHeadHistory> findByHousehold_IdOrderByChangedAtDescIdDesc(
            Long householdId,
            Pageable pageable);
}
