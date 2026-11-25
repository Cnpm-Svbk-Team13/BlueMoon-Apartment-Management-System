package com.bluemoon.bfms.model;

/**
 * Phân loại tình trạng cư trú theo danh mục của hệ thống.
 *
 * <p>Enum chỉ biểu diễn giá trị phân loại. Tính nhất quán của khoảng ngày hiện
 * được kiểm tra trên Entity {@link Resident}; DTO/Service quản lý cư trú phải
 * tiếp tục giữ cùng quy tắc khi luồng đó được mở rộng.</p>
 */
public enum ResidenceStatus {
    /** Thường trú. */
    THUONG_TRU,

    /** Tạm trú; nghiệp vụ cần quản lý thời hạn cư trú. */
    TAM_TRU,

    /** Tạm vắng; nghiệp vụ cần quản lý khoảng thời gian vắng mặt. */
    TAM_VANG
}
