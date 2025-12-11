package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Hotel {
    private long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
