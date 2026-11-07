package com.bluemoon.bfms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Validates the head profile for BE-03, US-001 AC 4 and RAW-002. */
public class HeadProfileForm {
    @NotBlank(message = "Please enter the full name.")
    @Size(max = 100, message = "Full name must contain at most 100 characters.")
    private String fullName;

    @NotBlank(message = "Please enter the national ID.")
    @Pattern(regexp = "[0-9]{12}", message = "National ID must contain 12 digits.")
    private String nationalId;

    @NotBlank(message = "Please enter the phone number.")
    @Pattern(regexp = "[0-9]{10}", message = "Phone number must contain 10 digits.")
    private String phone;

    @Email(regexp = "[^\\s@]+@[^\\s@]+\\.[^\\s@]+", message = "Please enter a valid email address.")
    @Size(max = 255, message = "Email must contain at most 255 characters.")
    private String email;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null || email.isBlank() ? null : email.strip();
    }
}
