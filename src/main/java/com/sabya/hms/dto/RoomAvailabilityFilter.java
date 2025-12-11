package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RoomAvailabilityFilter {
    private long hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private long roomTypeId;
    private int minOccupancy;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
