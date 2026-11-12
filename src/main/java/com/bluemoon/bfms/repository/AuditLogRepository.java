package com.bluemoon.bfms.repository;

import com.bluemoon.bfms.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Lưu và tra cứu nhật ký kiểm toán theo actor hoặc đối tượng bị tác động.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    /**
     * Lấy các thao tác do một tài khoản thực hiện, theo thứ tự mới nhất trước.
     *
     * @param userId ID tài khoản thực hiện
     * @return các nhật ký gắn với tài khoản
     */
    List<AuditLog> findAllByUser_IdOrderByCreatedAtDescIdDesc(Long userId);

    /**
     * Lấy lịch sử thao tác của một đối tượng; cặp entityName/entityId là khóa
     * định danh nghiệp vụ vì entity_id không phải FK đa hình.
     *
     * @param entityName tên loại đối tượng đã ghi trong nhật ký
     * @param entityId ID đối tượng
     * @return các nhật ký mới nhất trước
     */
    List<AuditLog> findAllByEntityNameAndEntityIdOrderByCreatedAtDescIdDesc(
            String entityName, Long entityId);
}
