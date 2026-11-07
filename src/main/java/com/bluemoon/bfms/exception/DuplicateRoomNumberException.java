package com.bluemoon.bfms.exception;

/** Reports duplicate room numbers for BE-02, US-001 AC 2. */
public class DuplicateRoomNumberException extends IllegalArgumentException {
    public DuplicateRoomNumberException() {
        super("Room number already exists.");
    }
}
