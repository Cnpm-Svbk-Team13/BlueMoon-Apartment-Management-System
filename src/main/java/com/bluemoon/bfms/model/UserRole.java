package com.bluemoon.bfms.model;

/**
 * Roles stored in users.role and used by Spring Security authorization.
 *
 * <p>The enum names intentionally match the PostgreSQL check constraint. Keep
 * EnumType.STRING on the entity mapping; ordinal storage would make
 * persisted roles dependent on declaration order.</p>
 */
public enum UserRole {
    ROLE_ADMIN,
    ROLE_ACCOUNTANT,
    ROLE_CASHIER,
    ROLE_RESIDENT;

    /**
     * Trả về chuỗi quyền tương ứng với tên enum để dùng khi tích hợp Spring Security.
     *
     * <p>Hiện giá trị trả về giống tên enum, ví dụ ROLE_ADMIN. Việc
     * tài khoản có được gán quyền này hay kiểm tra quyền ở request phải do
     * cấu hình/service bảo mật thực hiện.</p>
     *
     * Kết quả: tên quyền lưu trữ và sử dụng làm authority
     */
    public String authority() {
        return name();
    }

    /**
     * Kiểm tra vai trò có phải quản trị viên hệ thống hay không.
     *
     * Kết quả: true chỉ khi enum hiện tại là ROLE_ADMIN
     */
    public boolean isAdministrator() {
        return this == ROLE_ADMIN;
    }
}
