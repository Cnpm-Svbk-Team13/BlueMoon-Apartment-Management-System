package com.bluemoon.bfms.model;

/**
 * Residence classifications used by the current residents table.
 */
public enum ResidenceStatus {
    THUONG_TRU,
    TAM_TRU,
    TAM_VANG;

    /**
     * Xác định trạng thái cư trú có cần ngày bắt đầu và ngày kết thúc hay không.
     *
     * <p>TAM_TRU và TAM_VANG là trạng thái có thời hạn nên
     * yêu cầu đủ hai ngày. THUONG_TRU không dùng khoảng ngày trong
     * mô hình hiện tại.</p>
     *
     * Kết quả: true với tạm trú/tạm vắng; false với thường trú
     */
    public boolean requiresDateInterval() {
        return this != THUONG_TRU;
    }
}
