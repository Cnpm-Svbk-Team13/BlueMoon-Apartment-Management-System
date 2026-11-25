package com.bluemoon.bfms.model;

/**
 * Danh mục loại hình mặt bằng của căn hộ.
 *
 * <p>Giá trị enum được dùng trong mô hình Java và lưu thành mã ổn định trong
 * cơ sở dữ liệu. Ý nghĩa hiển thị tiếng Việt nên được xử lý ở giao diện, không
 * thay đổi tên mã theo nhãn hiển thị.</p>
 */
public enum ApartmentType {
    /** Ki-ốt thương mại dịch vụ. */
    KIOT,

    /** Mặt bằng thương mại hoặc dịch vụ. */
    COMMERCIAL,

    /** Căn hộ phục vụ mục đích ở. */
    RESIDENTIAL,

    /** Căn hộ penthouse ở tầng cao nhất. */
    PENTHOUSE
}