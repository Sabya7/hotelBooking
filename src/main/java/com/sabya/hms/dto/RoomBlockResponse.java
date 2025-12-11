package com.sabya.hms.dto;

import com.sabya.hms.domain.BlockType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomBlockResponse {
    private long id;
    private long roomId;
    private BlockType blockType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
