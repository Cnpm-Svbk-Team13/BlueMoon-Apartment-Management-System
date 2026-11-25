package com.bluemoon.bfms.model;

/**
 * Trạng thái của hộ gắn với căn hộ.
 *
 * <p>Trạng thái này mô tả vòng đời của hồ sơ hộ; không thay thế trạng thái
 * khai thác của căn hộ hoặc trạng thái cư trú của từng nhân khẩu.</p>
 */
public enum HouseholdStatus {
    /** Hộ hiện tại đang gắn với căn hộ. */
    ACTIVE,

    /** Hộ đã chuyển khỏi căn hộ. */
    MOVED_OUT,

    /** Hộ kết thúc do chuyển hộ theo nghiệp vụ. */
    TRANSFERRED
}
