package com.bluemoon.bfms.controller;

import com.bluemoon.bfms.dto.ResetPasswordForm;
import com.bluemoon.bfms.dto.UserForm;
import com.bluemoon.bfms.model.UserRole;
import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import com.bluemoon.bfms.service.ApartmentAccessService;
import com.bluemoon.bfms.service.UserService;
import com.bluemoon.bfms.service.UserService.UserSummary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Tiếp nhận các request quản trị tài khoản theo US-SEC-01 / NFR-02.
 *
 * <p>{@code @AuthenticationPrincipal} lấy {@link AuthenticatedPrincipal} từ
 * phiên hiện tại. Controller chuyển {@code userId} ổn định cho
 * {@link ApartmentAccessService} để kiểm tra quyền trong database, thay vì tra
 * lại actor theo username có thể đã thay đổi.</p>
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ApartmentAccessService accessService;

    public AdminController(
            UserService userService, ApartmentAccessService accessService) {
        this.userService = userService;
        this.accessService = accessService;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        Long actorId = requireAdmin(principal);
        model.addAttribute("users", userService.getAllUsers(actorId));
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(@AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        model.addAttribute("users", userService.getAllUsers(requireAdmin(principal)));
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String newUser(@AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        requireAdmin(principal);
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("roles", UserRole.values());
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String createUser(
            @Valid @ModelAttribute("userForm") UserForm form,
            BindingResult errors, @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request,
            RedirectAttributes redirect, Model model) {
        Long actorId = requireAdmin(principal);
        if (errors.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin tài khoản.");
            return "admin/user-form";
        }
        if (!passwordsMatch(form)) {
            errors.rejectValue("confirmPassword", "mismatch", "Hai mật khẩu chưa khớp.");
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("errorMessage", "Hai mật khẩu chưa khớp.");
            return "admin/user-form";
        }
        try {
            userService.createUser(
                    form.getUsername(), form.getFullName(), form.getEmail(), form.getPhone(),
                    form.getRole(), form.getPassword(), actorId, request.getRemoteAddr());
        } catch (IllegalArgumentException exception) {
            errors.reject("invalid", exception.getMessage());
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("errorMessage", exception.getMessage());
            return "admin/user-form";
        }
        redirect.addFlashAttribute("successMessage", "Tạo tài khoản thành công.");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        Long actorId = requireAdmin(principal);
        UserSummary summary = userService.getUserById(id, actorId);

        UserForm form = new UserForm();
        form.setUsername(summary.username());
        form.setFullName(summary.fullName());
        form.setEmail(summary.email());
        form.setPhone(summary.phone());
        form.setRole(summary.role());
        form.setActive(summary.active());

        model.addAttribute("userForm", form);
        model.addAttribute("userId", id);
        model.addAttribute("roles", UserRole.values());
        return "admin/user-form";
    }

    @PostMapping("/users/{id}")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("userForm") UserForm form,
            BindingResult errors,
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request,
            RedirectAttributes redirect,
            Model model) {
        Long actorId = requireAdmin(principal);
        if (errors.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin tài khoản.");
            return "admin/user-form";
        }
        try {
            userService.updateUser(
                    id,
                    form.getUsername(),
                    form.getFullName(),
                    form.getEmail(),
                    form.getPhone(),
                    form.getRole(),
                    Boolean.TRUE.equals(form.getActive()),
                    actorId,
                    request.getRemoteAddr());
        } catch (IllegalArgumentException exception) {
            errors.reject("invalid", exception.getMessage());
            model.addAttribute("userId", id);
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("errorMessage", exception.getMessage());
            return "admin/user-form";
        }
        redirect.addFlashAttribute("successMessage", "Cập nhật tài khoản thành công.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/status")
    public String updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean active,
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request,
            RedirectAttributes redirect) {
        return setUserStatus(id, active, principal, request, redirect);
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request, RedirectAttributes redirect) {
        return setUserStatus(id, true, principal, request, redirect);
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request, RedirectAttributes redirect) {
        return setUserStatus(id, false, principal, request, redirect);
    }

    @GetMapping("/users/{id}/reset-password")
    public String resetPasswordPage(@PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        requireAdmin(principal);
        model.addAttribute("resetPasswordForm", new ResetPasswordForm());
        model.addAttribute("accountId", id);
        return "admin/reset-password";
    }

    @PostMapping("/users/{id}/reset-password")
    public String resetPassword(
            @PathVariable Long id, @Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form,
            BindingResult errors, @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request, RedirectAttributes redirect, Model model) {
        Long actorId = requireAdmin(principal);
        model.addAttribute("accountId", id);
        if (errors.hasErrors()) {
            return "admin/reset-password";
        }
        try {
            userService.resetPassword(id, form.getNewPassword(), actorId, request.getRemoteAddr());
        } catch (IllegalArgumentException exception) {
            errors.reject("invalid", exception.getMessage());
            return "admin/reset-password";
        }
        redirect.addFlashAttribute("successMessage", "Cấp lại mật khẩu thành công.");
        return "redirect:/admin/users";
    }

    private Long requireAdmin(AuthenticatedPrincipal principal) {
        return accessService.requireAdminId(principal == null ? null : principal.userId());
    }

    private boolean passwordsMatch(UserForm form) {
        return form.getPassword() != null
                && form.getPassword().equals(form.getConfirmPassword());
    }

    private String setUserStatus(Long id, boolean active, AuthenticatedPrincipal principal,
            HttpServletRequest request, RedirectAttributes redirect) {
        userService.toggleUserStatus(
                id, active, requireAdmin(principal), request.getRemoteAddr());
        redirect.addFlashAttribute("successMessage", "Cập nhật trạng thái tài khoản thành công.");
        return "redirect:/admin/users";
    }
}
