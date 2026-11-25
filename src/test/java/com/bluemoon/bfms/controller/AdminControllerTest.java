package com.bluemoon.bfms.controller;

import com.bluemoon.bfms.dto.ResetPasswordForm;
import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import com.bluemoon.bfms.service.ApartmentAccessService;
import com.bluemoon.bfms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private ApartmentAccessService accessService;

    @Mock
    private HttpServletRequest request;

    private AdminController controller;
    private final AuthenticatedPrincipal principal = new AuthenticatedPrincipal(501L, "admin_test");

    @BeforeEach
    void setUp() {
        controller = new AdminController(userService, accessService);
        when(accessService.requireAdminId(501L)).thenReturn(501L);
    }

    @Test
    void preservesAccountIdWhenDtoValidationFails() {
        ResetPasswordForm form = new ResetPasswordForm();
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(form, "resetPasswordForm");
        errors.rejectValue("newPassword", "invalid");
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.resetPassword(
                502L, form, errors, principal, request, new RedirectAttributesModelMap(), model);

        assertEquals("admin/reset-password", view);
        assertEquals(502L, model.getAttribute("accountId"));
        assertTrue(errors.hasErrors());
    }

    @Test
    void preservesAccountIdWhenServiceRejectsPasswordReset() {
        ResetPasswordForm form = new ResetPasswordForm();
        form.setNewPassword("valid-password");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(form, "resetPasswordForm");
        ExtendedModelMap model = new ExtendedModelMap();
        doThrow(new IllegalArgumentException("Account does not exist."))
                .when(userService).resetPassword(502L, "valid-password", 501L, null);

        String view = controller.resetPassword(
                502L, form, errors, principal, request, new RedirectAttributesModelMap(), model);

        assertEquals("admin/reset-password", view);
        assertEquals(502L, model.getAttribute("accountId"));
        assertTrue(errors.hasErrors());
    }
}
