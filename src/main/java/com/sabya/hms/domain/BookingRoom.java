package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRoom {
    private Booking booking;
    private Room room;
}
