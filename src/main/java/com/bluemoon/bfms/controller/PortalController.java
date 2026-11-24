package com.bluemoon.bfms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Điều hướng trang gốc theo quyền đã được xác thực và cung cấp route portal.
 *
 * <p>Controller không tự xác thực thông tin đăng nhập; Spring Security đã xử
 * lý việc đó trước khi request đến MVC. Trang portal hiện là điểm vào giao diện,
 * chưa đồng nghĩa các nghiệp vụ cư dân đã được triển khai.</p>
 */
@Controller
public class PortalController {
    /**
     * Chuyển người dùng tới khu vực phù hợp với role trong Authentication.
     *
     * @param authentication danh tính và quyền do Spring Security gắn vào request
     * @return tên view hoặc URL điều hướng theo role
     */
    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin";
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ACCOUNTANT"))) {
            return "redirect:/admin/apartments";
        }
        return "portal/home";
    }

    /**
     * Hiển thị trang portal mặc định.
     *
     * @return tên template portal
     */
    @GetMapping("/portal")
    public String portal() {
        return "portal/home";
    }
}
