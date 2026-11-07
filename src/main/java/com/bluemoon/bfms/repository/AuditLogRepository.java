package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Truy vấn nhật ký chỉ đọc và giới hạn kết quả bằng phân trang. */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "user.person"})
    Optional<AuditLog> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"user", "user.person"})
    List<AuditLog> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "user.person"})
    Page<AuditLog> findAll(Pageable pageable);

    /**
     * Liệt kê nhật ký mới nhất trước, tải thông tin người thực hiện cùng truy vấn.
     *
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang nhật ký
     */
    @EntityGraph(attributePaths = {"user", "user.person"})
    Page<AuditLog> findAllByOrderByCreatedAtDescIdDesc(Pageable pageable);

    /**
     * Tìm nhật ký theo loại và ID đối tượng, kèm thông tin người thực hiện.
     *
     * Tham số entityName: tên đối tượng đã ghi trong nhật ký
     * Tham số entityId: ID đối tượng
     * Tham số pageable: cấu hình phân trang và sắp xếp
     * Kết quả: trang nhật ký khớp với đối tượng
     */
    @EntityGraph(attributePaths = {"user", "user.person"})
    Page<AuditLog> findByEntityNameIgnoreCaseAndEntityIdOrderByCreatedAtDescIdDesc(
            String entityName,
            Long entityId,
            Pageable pageable);
}
