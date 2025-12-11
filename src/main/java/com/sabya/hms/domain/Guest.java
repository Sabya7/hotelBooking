package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Guest {
    private long id;
    private String name;
    private String address;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
