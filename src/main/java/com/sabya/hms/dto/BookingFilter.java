package com.sabya.hms.dto;

import com.sabya.hms.domain.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BookingFilter {
    private long hotelId;

    private LocalDate checkInStart;
    private LocalDate checkInEnd;
    private LocalDate checkOutStart;
    private LocalDate checkOutEnd;

    private LocalDate overlapStart;
    private LocalDate overlapEnd;

    private BookingStatus status;
    private List<BookingStatus> statuses;

    private Long guestId;
    private Long roomId;

    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    private Integer minGuests;
    private Integer maxGuests;
}
