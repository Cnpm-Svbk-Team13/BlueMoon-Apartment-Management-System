package com.bluemoon.bfms.model;

/**
 * Các quyền lưu trong cột {@code users.role} của schema Sprint 1.
 *
 * <p>Enum được lưu bằng tên để giá trị trong database ổn định, dễ đọc và khớp
 * trực tiếp với các mã vai trò mà Spring Security sử dụng.</p>
 */
public enum UserRole {
    ROLE_ADMIN,
    ROLE_ACCOUNTANT,
    ROLE_CASHIER,
    ROLE_RESIDENT
}
