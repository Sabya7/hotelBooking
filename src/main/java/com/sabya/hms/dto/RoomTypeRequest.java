package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomTypeRequest {
    private String name;
    private int maxOccupancy;
    private BigDecimal baseRate;
    private long hotelId;
}
