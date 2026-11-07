package com.bluemoon.bfms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Reports missing apartments in BE-02 and BE-03 (US-001). */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApartmentNotFoundException extends RuntimeException {
    public ApartmentNotFoundException() {
        super("Apartment not found.");
    }
}
