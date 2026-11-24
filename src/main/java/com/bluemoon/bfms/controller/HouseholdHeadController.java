package com.bluemoon.bfms.controller;

import com.bluemoon.bfms.dto.HouseholdHeadForm;
import com.bluemoon.bfms.model.RelationshipWithHead;
import com.bluemoon.bfms.security.AuthenticatedPrincipal;
import com.bluemoon.bfms.service.ApartmentAccessService;
import com.bluemoon.bfms.service.HouseholdDetailsService;
import com.bluemoon.bfms.service.HouseholdHeadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * Điều phối form tra cứu, gán, thay thế và cập nhật hồ sơ chủ hộ theo BE-03,
 * US-001 AC 3-4 và NFR-02.
 *
 * <p>Principal được Spring Security đưa vào handler qua
 * {@code @AuthenticationPrincipal}. Controller dùng ID ổn định từ principal
 * làm actor; Service chịu trách nhiệm lưu thay đổi, lịch sử và audit trong
 * transaction.</p>
 */
@Controller
@RequestMapping("/admin/apartments/{id}/household")
public class HouseholdHeadController {
    private final ApartmentAccessService access;
    private final HouseholdDetailsService details;
    private final HouseholdHeadService heads;

    public HouseholdHeadController(
            ApartmentAccessService access, HouseholdDetailsService details, HouseholdHeadService heads) {
        this.access = access;
        this.details = details;
        this.heads = heads;
    }

    @GetMapping
    public String show(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean editProfile,
            @AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        requireEditor(principal);
        var apartment = details.getDetails(id);
        var form = new HouseholdHeadForm();
        form.setExpectedHeadPersonId(apartment.headPersonId());
        boolean editing = editProfile && apartment.currentHead() != null;
        if (editing) {
            form.setHeadPerson(apartment.currentHead());
        }
        model.addAttribute("headForm", form);
        model.addAttribute("apartment", apartment);
        return view(id, editing, model);
    }

    @PostMapping("/lookup")
    public String lookup(@PathVariable Long id, @ModelAttribute("headForm") HouseholdHeadForm form,
            BindingResult errors, @AuthenticationPrincipal AuthenticatedPrincipal principal, Model model) {
        requireEditor(principal);
        form.setConfirmedPersonId(null);
        try {
            var existing = details.findExistingPerson(form.getHeadPerson().getNationalId());
            if (existing.isPresent()) {
                model.addAttribute("existingPerson", existing.get());
                form.setHeadPerson(existing.get().profile());
            } else {
                model.addAttribute("lookupMessage", "No existing person was found. Enter a new profile.");
            }
        } catch (IllegalArgumentException exception) {
            errors.rejectValue("headPerson.nationalId", "invalid", exception.getMessage());
        }
        return view(id, false, model);
    }

    @PostMapping
    public String assign(@PathVariable Long id, @Valid @ModelAttribute("headForm") HouseholdHeadForm form,
            BindingResult errors, @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request,
            Model model, RedirectAttributes redirect) {
        Long actorId = requireEditor(principal);
        return save(id, false, errors, model, redirect,
                () -> heads.assignOrReplace(id, form, actorId, request.getRemoteAddr()));
    }

    @PostMapping("/profile")
    public String updateProfile(@PathVariable Long id,
            @Valid @ModelAttribute("headForm") HouseholdHeadForm form, BindingResult errors,
            @AuthenticationPrincipal AuthenticatedPrincipal principal,
            HttpServletRequest request, Model model, RedirectAttributes redirect) {
        Long actorId = requireEditor(principal);
        return save(id, true, errors, model, redirect,
                () -> heads.updateProfile(id, form, actorId, request.getRemoteAddr()));
    }

    private String save(Long id, boolean editing, BindingResult errors, Model model,
            RedirectAttributes redirect, Runnable operation) {
        if (!errors.hasErrors()) {
            try {
                operation.run();
                redirect.addFlashAttribute("successMessage", "Household head information saved.");
                redirect.addAttribute("id", id);
                return "redirect:/admin/apartments/{id}/household";
            } catch (IllegalArgumentException exception) {
                errors.reject("invalid", exception.getMessage());
            } catch (DataAccessException | TransactionException exception) {
                errors.reject("storage", "The household change could not be saved. Please review the details.");
            }
        }
        return view(id, editing, model);
    }

    private Long requireEditor(AuthenticatedPrincipal principal) {
        return access.requireEditorId(principal == null ? null : principal.userId());
    }

    private String view(Long id, boolean editing, Model model) {
        if (!model.containsAttribute("apartment")) {
            model.addAttribute("apartment", details.getDetails(id));
        }
        model.addAttribute("editing", editing);
        model.addAttribute("relationships", Arrays.stream(RelationshipWithHead.values())
                .filter(relationship -> relationship != RelationshipWithHead.HEAD).toList());
        return "admin/household-head";
    }
}
