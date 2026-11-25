package com.bluemoon.bfms.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * Danh tính tài khoản được lưu trong phiên Spring Security.
 *
 * <p>{@code userId} là khóa ổn định dùng để kiểm tra quyền hiện tại và ghi audit.
 * Username chỉ được giữ để biểu diễn tên principal; không dùng username để tra
 * lại actor vì username có thể thay đổi hoặc được cấp lại cho tài khoản khác.
 * Lớp triển khai {@link Serializable} để principal có thể được tuần tự hóa cùng
 * phiên nếu cơ chế lưu session yêu cầu.</p>
 *
 * @param userId ID bất biến của tài khoản đã xác thực
 * @param username tên đăng nhập tại thời điểm xác thực
 */
public record AuthenticatedPrincipal(Long userId, String username) implements Principal, Serializable {
    /**
     * Tạo principal sau khi xác thực và từ chối dữ liệu danh tính không hợp lệ.
     *
     * @param userId ID bất biến của tài khoản đã xác thực
     * @param username tên đăng nhập tại thời điểm xác thực
     * @throws IllegalArgumentException nếu thiếu ID hợp lệ hoặc username rỗng
     */
    public AuthenticatedPrincipal {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("A valid authenticated account ID is required.");
        }
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("An authenticated username is required.");
        }
    }

    /**
     * Trả username tại thời điểm đăng nhập theo hợp đồng {@link Principal}.
     *
     * <p>Không dùng giá trị này làm actor ID; các thao tác cần danh tính phải
     * đọc {@link #userId()}.</p>
     *
     * @return username được lưu trong principal
     */
    @Override
    public String getName() {
        return username;
    }
}
