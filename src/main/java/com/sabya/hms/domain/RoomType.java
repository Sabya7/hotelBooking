package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomType {
    private long id;
    private String name;
    private Hotel hotel;
    private int maxOccupancy;
    private BigDecimal baseRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
