package com.bluemoon.bfms.controller;

import com.bluemoon.bfms.dto.ApartmentForm;
import com.bluemoon.bfms.exception.DuplicateRoomNumberException;
import com.bluemoon.bfms.model.ApartmentType;
import com.bluemoon.bfms.model.ApartmentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import com.bluemoon.bfms.service.ApartmentAccessService;
import com.bluemoon.bfms.service.ApartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/** Handles apartment creation for BE-02, US-001 AC 1-2 and NFR-02. */
@Controller
@RequestMapping("/admin/apartments")
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final ApartmentAccessService accessService;

    public ApartmentController(ApartmentService apartmentService, ApartmentAccessService accessService) {
        this.apartmentService = apartmentService;
        this.accessService = accessService;
    }

    @ModelAttribute("apartmentTypes")
    public ApartmentType[] apartmentTypes() {
        return ApartmentType.values();
    }

    @GetMapping
    public String list(@RequestParam(required = false) Integer floor, Principal principal, Model model) {
        accessService.requireEditorId(principal == null ? null : principal.getName());
        try {
            model.addAttribute("apartments", apartmentService.listApartments(floor));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
        model.addAttribute("floor", floor);
        return "admin/apartments";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Principal principal, Model model) {
        accessService.requireEditorId(principal == null ? null : principal.getName());
        model.addAttribute("apartmentForm", apartmentService.getApartmentForEdit(id));
        model.addAttribute("apartmentId", id);
        model.addAttribute("apartmentStatuses", ApartmentStatus.values());
        return "admin/apartment-form";
    }

    @GetMapping("/new")
    public String showCreateForm(Principal principal, Model model) {
        accessService.requireEditorId(principal == null ? null : principal.getName());
        model.addAttribute("apartmentForm", new ApartmentForm());
        return "admin/apartment-form";
    }

    @PostMapping
    public String createApartment(
            @Valid @ModelAttribute("apartmentForm") ApartmentForm form,
            BindingResult errors, Principal principal, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        Long actorId = accessService.requireEditorId(principal == null ? null : principal.getName());
        return save(errors, redirectAttributes, "Apartment created successfully.",
                () -> apartmentService.createApartment(form, actorId, request.getRemoteAddr()));
    }

    @PostMapping("/{id}")
    public String updateApartment(@PathVariable Long id,
            @Valid @ModelAttribute("apartmentForm") ApartmentForm form, BindingResult errors,
            Principal principal, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        Long actorId = accessService.requireEditorId(principal == null ? null : principal.getName());
        model.addAttribute("apartmentId", id);
        model.addAttribute("apartmentStatuses", ApartmentStatus.values());
        return save(errors, redirectAttributes, "Apartment updated successfully.",
                () -> apartmentService.updateApartment(id, form, actorId, request.getRemoteAddr()));
    }

    private String save(BindingResult errors, RedirectAttributes redirectAttributes,
            String successMessage, Runnable operation) {
        if (errors.hasErrors()) {
            return "admin/apartment-form";
        }
        try {
            operation.run();
        } catch (DuplicateRoomNumberException exception) {
            errors.rejectValue("roomNumber", "duplicate", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            errors.reject("invalid", exception.getMessage());
        } catch (DataAccessException exception) {
            errors.reject("storage", "The apartment could not be saved. Please try again.");
        }
        if (errors.hasErrors()) {
            return "admin/apartment-form";
        }
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/admin/apartments";
    }
}
