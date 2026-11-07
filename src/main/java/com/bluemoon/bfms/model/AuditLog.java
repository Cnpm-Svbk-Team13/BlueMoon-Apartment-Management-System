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
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.Immutable;
import org.hibernate.generator.EventType;
import java.time.OffsetDateTime;

/** JPA entity nhật ký chỉ-ghi-thêm; không đưa thông tin xác thực hay định danh nhạy cảm vào details. */
@Entity
@Immutable
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

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected AuditLog() {
    }

    public AuditLog(
            Long id,
            User user,
            String action,
            String entityName,
            Long entityId,
            String details,
            String ipAddress,
            OffsetDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.action = action;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long id() { return id; }
    public User user() { return user; }
    public Long userId() { return user == null ? null : user.id(); }
    public String action() { return action; }
    public String entityName() { return entityName; }
    public Long entityId() { return entityId; }
    public String details() { return details; }
    public String ipAddress() { return ipAddress; }
    public OffsetDateTime createdAt() { return createdAt; }

    /**
     * Kiểm tra các trường cơ bản của một sự kiện nhật ký.
     *
     * <p>Hành động và tên đối tượng phải có nội dung, không vượt giới hạn
     * schema; các ID nếu có phải dương; địa chỉ IP nếu có không vượt quá 50
     * ký tự. Phương thức không quét nội dung details để phát hiện
     * thông tin nhạy cảm, vì vậy service vẫn phải tránh ghi mật khẩu, hash
     * hoặc định danh cá nhân đầy đủ.</p>
     *
     * Có thể ném IllegalArgumentException nếu trường bắt buộc hoặc ID/độ dài không hợp lệ
     */
    public void validate() {
        if (user != null && (user.id() == null || user.id() <= 0)) {
            throw new IllegalArgumentException("userId must be positive when present");
        }
        requireText(action, "action", 50);
        requireText(entityName, "entityName", 50);
        if (entityId != null && entityId <= 0) {
            throw new IllegalArgumentException("entityId must be positive when present");
        }
        if (ipAddress != null && ipAddress.length() > 50) {
            throw new IllegalArgumentException("ipAddress must not exceed 50 characters");
        }
    }

    /**
     * Kiểm tra sự kiện có gắn với một tài khoản người dùng xác định hay không.
     *
     * Kết quả: true nếu userId có giá trị; tài khoản chưa xác định được biểu diễn bằng null
     */
    public boolean hasActor() {
        return user != null && user.id() != null;
    }

    /**
     * Kiểm tra sự kiện có nói đến đúng tên và ID bản ghi được cung cấp hay không.
     *
     * <p>So sánh tên không phân biệt chữ hoa/chữ thường; ID được so sánh chính
     * xác. Nếu một tham số hoặc trường tương ứng là null, kết quả là
     * false.</p>
     *
     * Tham số name: tên đối tượng cần so sánh
     * Tham số recordId: ID bản ghi cần so sánh
     * Kết quả: true khi cả tên lẫn ID đều khớp
     */
    public boolean concerns(String name, Long recordId) {
        return name != null && recordId != null
                && name.equalsIgnoreCase(entityName)
                && recordId.equals(entityId);
    }

    /**
     * Kiểm tra chuỗi bắt buộc có nội dung và không vượt quá độ dài cho phép.
     *
     * Tham số value: chuỗi cần kiểm tra
     * Tham số field: tên trường dùng trong thông báo lỗi
     * Tham số maxLength: số ký tự tối đa
     * Có thể ném IllegalArgumentException nếu chuỗi null, rỗng hoặc quá dài
     */
    private static void requireText(String value, String field, int maxLength) {
        if (value == null || value.isBlank() || value.length() > maxLength) {
            throw new IllegalArgumentException(field + " must be non-blank and at most "
                    + maxLength + " characters");
        }
    }
}
