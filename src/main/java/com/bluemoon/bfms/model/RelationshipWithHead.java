package com.bluemoon.bfms.model;

/**
 * Relationship values accepted by the current residents table constraint.
 */
public enum RelationshipWithHead {
    HEAD,
    SPOUSE,
    CHILD,
    PARENT,
    TENANT,
    OTHER;

    /**
     * Kiểm tra giá trị quan hệ hiện tại có xác định người này là chủ hộ hay không.
     *
     * Kết quả: true nếu đây là HEAD; nếu không trả về false
     */
    public boolean isHead() {
        return this == HEAD;
    }
}
