package com.bluemoon.bfms.model;

/**
 * Household states accepted by the PostgreSQL schema.
 */
public enum HouseholdStatus {
    ACTIVE,
    MOVED_OUT,
    TRANSFERRED;

    /**
     * Kiểm tra trạng thái hiện tại có biểu thị hộ đang hoạt động hay không.
     *
     * Kết quả: true nếu đây là ACTIVE; nếu không trả về false
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
}
