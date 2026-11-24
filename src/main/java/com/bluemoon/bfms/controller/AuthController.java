package com.bluemoon.bfms.controller;

import com.bluemoon.bfms.dto.ChangePasswordForm;
import com.bluemoon.bfms.dto.LoginForm;
import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import com.bluemoon.bfms.service.ApartmentAccessService;
import com.bluemoon.bfms.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Điều phối các route đăng nhập và đổi mật khẩu.
 *
 * <p>Đăng nhập POST do Spring Security filter xử lý. Với thao tác đổi mật khẩu,
 * {@code @AuthenticationPrincipal} yêu cầu Spring lấy principal hiện tại từ
 * {@code SecurityContext} và đưa vào tham số handler. Annotation chỉ cung cấp
 * danh tính; {@link ApartmentAccessService} vẫn phải kiểm tra tài khoản còn
 * hoạt động trong database trước khi service nghiệp vụ thực hiện thay đổi.</p>
 *
 * <p>Quy tắc khóa tài khoản, BCrypt và audit thuộc {@link AuthService}, không
 * đặt trong Controller.</p>
 */
@Controller
public class AuthController {
    private final AuthService authService;
    private final ApartmentAccessService accessService;

    public AuthController(AuthService authService, ApartmentAccessService accessService) {
        this.authService = authService;
        this.accessService = accessService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "auth/login";
    }

    @GetMapping({"/change-password", "/admin/users/change-password"})
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "admin/change-password";
    }

    @PostMapping({"/change-password", "/admin/users/change-password"})
    public String changePassword(
            @Valid @ModelAttribute("changePasswordForm") ChangePasswordForm form,
            BindingResult errors, @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirect) {
        if (errors.hasErrors()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ và kiểm tra lại mật khẩu.");
            return "admin/change-password";
        }
        Long userId = accessService.requireActiveAccountId(
                principal == null ? null : principal.userId());
        try {
            authService.changePassword(
                    userId, form.getCurrentPassword(), form.getNewPassword(),
                    form.getConfirmPassword(), request.getRemoteAddr());
        } catch (IllegalArgumentException exception) {
            errors.reject("invalid", exception.getMessage());
            model.addAttribute("errorMessage", exception.getMessage());
            return "admin/change-password";
        }
        redirect.addFlashAttribute("successMessage", "Đổi mật khẩu thành công.");
        return "redirect:/";
    }
}
