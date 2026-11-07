package com.bluemoon.bfms.service;

import com.bluemoon.bfms.repository.ApartmentActorRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Checks apartment editor access for BE-02, US-001 and NFR-02. */
@Service
public class ApartmentAccessService {
    private final ApartmentActorRepository actorRepository;

    public ApartmentAccessService(ApartmentActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    /**
     * Resolves an active editor from the authenticated server-side username.
     * @param username authenticated username
     * @return database user ID for the audit record
     * @throws AccessDeniedException if the user cannot manage apartments
     */
    @Transactional(readOnly = true)
    public Long requireEditorId(String username) {
        return actorRepository.findActiveEditorId(username)
                .orElseThrow(() -> new AccessDeniedException("Apartment editor access is required."));
    }
}
