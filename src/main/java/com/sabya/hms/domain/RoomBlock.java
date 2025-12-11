package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomBlock {
    private long id;
    private Room room;
    private BlockType blockType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
