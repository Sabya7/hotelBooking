package com.sabya.hms.dto;

import com.sabya.hms.domain.RoomStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoomResponse {
    private long id;
    private String roomNumber;
    private long roomTypeId;
    private RoomStatus status;
    private long hotelId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
