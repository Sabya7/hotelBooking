package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomTypeResponse {
    private long id;
    private String name;
    private int maxOccupancy;
    private BigDecimal baseRate;
    private long hotelId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
