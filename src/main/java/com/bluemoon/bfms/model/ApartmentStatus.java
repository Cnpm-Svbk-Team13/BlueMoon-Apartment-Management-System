package com.bluemoon.bfms.model;

/**
 * Trạng thái khai thác hiện tại của một căn hộ.
 *
 * <p>{@code VACANT} biểu thị căn đang để trống; {@code OCCUPIED} biểu thị căn
 * đang có người sử dụng. Đây là trạng thái căn hộ, không phải trạng thái cư trú
 * của từng người.</p>
 */
public enum ApartmentStatus {
    /** Căn hộ hiện không có người sử dụng. */
    VACANT,

    /** Căn hộ hiện đang có người sử dụng. */
    OCCUPIED
}
