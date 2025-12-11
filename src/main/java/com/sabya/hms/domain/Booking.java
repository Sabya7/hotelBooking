package com.sabya.hms.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Booking {

    private long id;
    private Guest bookedBy;
    private List<BookingRoom> bookingRooms;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Hotel hotel;
    private int numberOfGuests;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
