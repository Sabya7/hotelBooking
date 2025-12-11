package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RoomAvailabilityResponse {
    private long roomId;
    private boolean available;
    private String reason;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ConflictDetails conflictDetails;

    @Data
    @Builder
    public static class ConflictDetails {
        private String type;
        private Long bookingId;
        private Long blockId;
        private LocalDate conflictStart;
        private LocalDate conflictEnd;
    }
}
