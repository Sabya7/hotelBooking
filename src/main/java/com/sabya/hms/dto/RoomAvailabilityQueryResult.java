package com.sabya.hms.dto;

import java.time.LocalDate;

public interface RoomAvailabilityQueryResult {
    public Boolean getAvailable();
    public String getConflictType();
    public Long getBookingId();
    public Long getBlockId();
    public LocalDate getConflictStart();
    public LocalDate getConflictEnd();
}
