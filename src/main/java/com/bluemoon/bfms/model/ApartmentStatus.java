package com.bluemoon.bfms.model;

/**
 * Apartment occupancy states stored by the current Sprint 1 schema.
 */
public enum ApartmentStatus {
    VACANT,
    OCCUPIED;

    /**
     * Kiểm tra trạng thái hiện tại có biểu thị căn hộ đang được sử dụng hay không.
     *
     * Kết quả: true nếu đây là OCCUPIED; nếu không trả về false
     */
    public boolean isOccupied() {
        return this == OCCUPIED;
    }
}
