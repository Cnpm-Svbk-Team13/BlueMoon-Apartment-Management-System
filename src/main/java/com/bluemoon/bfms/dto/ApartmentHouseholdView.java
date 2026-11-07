package com.bluemoon.bfms.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.OffsetDateTime;
import java.util.List;

/** Read model for apartment head details and historical snapshots, BE-03. */
public record ApartmentHouseholdView(
        Long apartmentId, String roomNumber, Long headPersonId, HeadProfileForm currentHead,
        boolean previousHeadResidence, List<HistoryEntry> history) {

    public record HistoryEntry(
            String eventType, JsonNode previousSnapshot, JsonNode newSnapshot,
            OffsetDateTime changedAt, Long changedByUserId) {
    }
}
