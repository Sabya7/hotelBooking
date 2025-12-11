package com.sabya.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HotelResponse {
    private long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
