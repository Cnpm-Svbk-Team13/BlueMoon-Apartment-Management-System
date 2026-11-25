package com.bluemoon.bfms.model;

/**
 * Quan hệ của một thành viên trong hộ so với chủ hộ.
 *
 * <p>Đây là danh mục đóng để tránh lưu các cách diễn đạt tự do, không đồng nhất
 * trong cơ sở dữ liệu.</p>
 */
public enum RelationshipWithHead {
    /** Thành viên là chủ hộ đại diện. */
    HEAD,

    /** Vợ hoặc chồng của chủ hộ. */
    SPOUSE,

    /** Con của chủ hộ. */
    CHILD,

    /** Cha hoặc mẹ của chủ hộ. */
    PARENT,

    /** Người thuê nhà. */
    TENANT,

    /** Quan hệ khác không nằm trong các nhóm đã định nghĩa. */
    OTHER
}
