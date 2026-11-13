package com.bluemoon.bfms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

/**
 * Ánh xạ một bản ghi nhật ký kiểm toán trong bảng {@code audit_logs}.
 *
 * <p>Người thực hiện được ánh xạ thành quan hệ tới {@link User}, tương ứng
 * khóa ngoại {@code user_id}. Giá trị này có thể null khi chưa xác định được
 * tài khoản; nhật ký không lưu mật khẩu, hash mật khẩu hoặc dữ liệu định danh
 * cá nhân đầy đủ.</p>
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "entity_name", nullable = false, length = 50)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "details", columnDefinition = "text")
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected AuditLog() {
    }

    /**
     * Tạo bản ghi kiểm toán; khóa chính và thời điểm tạo do database sinh.
     *
     * @param user tài khoản thực hiện, hoặc null nếu chưa xác định được
     * @param action mã hành động được ghi nhận
     * @param entityName tên loại đối tượng bị tác động
     * @param entityId khóa đối tượng bị tác động, hoặc null nếu không có
     * @param details mô tả tùy chọn, không chứa bí mật/dữ liệu cá nhân đầy đủ
     * @param ipAddress địa chỉ IP tùy chọn của client
     */
    public AuditLog(
            User user,
            String action,
            String entityName,
            Long entityId,
            String details,
            String ipAddress) {

        this.user = user;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.ipAddress = ipAddress;
    }

    public Long getId() {
        return id;
    }

    /** @return tài khoản liên kết hoặc null nếu actor chưa xác định */
    public User getUser() {
        return user;
    }

    /**
     * Trả ID để duy trì read-model hiện tại mà không buộc nơi gọi tải toàn bộ
     * hồ sơ tài khoản.
     *
     * @return ID tài khoản hoặc null nếu nhật ký chưa có actor
     */
    public Long getUserId() {
        return user == null ? null : user.getId();
    }

    public String getAction() {
        return action;
    }

    public String getEntityName() {
        return entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
