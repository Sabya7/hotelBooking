package com.sabya.hms.dto;

import com.sabya.hms.domain.RoomStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomRequest {
    private String roomNumber;
    private long roomTypeId;
    private RoomStatus status;
    private long hotelId;
}
