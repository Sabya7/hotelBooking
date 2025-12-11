package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Room {
    private long id;
    private String roomNumber;
    private RoomType roomType;
    private RoomStatus status;
    private Hotel hotel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
