package com.bluemoon.bfms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortalController {
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

    @GetMapping("/portal")
    public String portal() {
        return "portal/home";
    }
}
