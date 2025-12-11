package com.sabya.hms.dto;

import com.sabya.hms.domain.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private long id;
    private long guestId;
    private long hotelId;
    private List<Long> roomIds;
    private int numberOfGuests;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
