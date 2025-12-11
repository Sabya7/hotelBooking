package com.sabya.hms.dto;

import com.sabya.hms.domain.BlockType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RoomBlockRequest {
    private long roomId;
    private BlockType blockType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
