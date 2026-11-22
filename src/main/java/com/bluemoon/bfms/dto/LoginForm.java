package com.bluemoon.bfms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dữ liệu form đăng nhập. Mật khẩu thô chỉ tồn tại trong request và không được lưu audit/entity.
 */
public class LoginForm {
    @NotBlank(message = "Vui lòng nhập tên đăng nhập.")
    @Size(max = 50)
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
