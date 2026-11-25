package com.bluemoon.bfms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO quản trị viên cấp lại mật khẩu cho tài khoản BQT. */
public class ResetPasswordForm {
    @NotBlank
    @Size(min = 8, max = 72)
    private String newPassword;

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
