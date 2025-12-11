package com.sabya.hms.controller;

import com.sabya.hms.domain.Booking;
import com.sabya.hms.dto.BookingFilter;
import com.sabya.hms.dto.BookingRequest;
import com.sabya.hms.dto.BookingResponse;
import com.sabya.hms.mapper.BookingMapper;
import com.sabya.hms.service.BookingQueryService;
import com.sabya.hms.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    private final BookingQueryService bookingQueryService;

    public BookingController(BookingService bookingService, BookingMapper bookingMapper, BookingQueryService bookingQueryService) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping
    public BookingResponse create(@RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingMapper.toDomain(bookingRequest);
        Booking savedBooking = bookingService.create(booking);
        return bookingMapper.toResponseDTO(savedBooking);
    }

    @GetMapping("/{id}")
    public BookingResponse get(@PathVariable long id) {
        Booking booking = bookingService.get(id);
        return bookingMapper.toResponseDTO(booking);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable long id, @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingMapper.toDomain(bookingRequest);
        booking.setId(id);
        Booking updatedBooking = bookingService.update(booking);
        return bookingMapper.toResponseDTO(updatedBooking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        bookingService.delete(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<BookingResponse> findAllBookingsForAGuest(@PathVariable long guestId, @RequestParam long hotelId, @RequestParam(required = false, defaultValue = "false") boolean includeCancelled) {
        return bookingQueryService.findAllBookingsForAGuest(hotelId, guestId, includeCancelled).stream().map(bookingMapper::toResponseDTO).toList();
    }

    @PostMapping("/search")
    public List<BookingResponse> searchBookings(@RequestBody BookingFilter filter) {
        return bookingQueryService.searchBookings(filter).stream()
                .map(bookingMapper::toResponseDTO)
                .toList();
    }
}
