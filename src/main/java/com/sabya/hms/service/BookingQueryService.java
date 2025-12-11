package com.sabya.hms.service;

import com.sabya.hms.domain.Booking;
import com.sabya.hms.domain.BookingStatus;
import com.sabya.hms.dto.BookingFilter;
import com.sabya.hms.entity.BookingEntity;
import com.sabya.hms.mapper.BookingMapper;
import com.sabya.hms.repository.BookingRepository;
import com.sabya.hms.specification.BookingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingQueryService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingQueryService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Transactional(readOnly = true)
    public List<Booking> findAllBookingsForAGuest(long hotelId, long guestId, boolean includeCancelled) {
        List<Booking> bookings = bookingRepository.findByHotelIdAndBookedByIdOrderByCreatedAtDesc(hotelId, guestId).stream().map(bookingMapper::toDomain).toList();
        if(!includeCancelled)
            bookings =  bookings.stream().filter(booking -> booking.getStatus() != BookingStatus.CANCELLED).toList();

        return bookings;
    }

    @Transactional(readOnly = true)
    public List<Booking> searchBookings(BookingFilter filter) {
        Specification<BookingEntity> spec = Specification
                .where(BookingSpecification.hasHotel(filter.getHotelId()))
                .and(BookingSpecification.hasStatus(filter.getStatus()))
                .and(BookingSpecification.hasStatuses(filter.getStatuses()))
                .and(BookingSpecification.hasGuest(filter.getGuestId()))
                .and(BookingSpecification.hasRoom(filter.getRoomId()))
                .and(BookingSpecification.checkInBetween(filter.getCheckInStart(), filter.getCheckInEnd()))
                .and(BookingSpecification.checkOutBetween(filter.getCheckOutStart(), filter.getCheckOutEnd()))
                .and(BookingSpecification.overlapsWith(filter.getOverlapStart(), filter.getOverlapEnd()))
                .and(BookingSpecification.amountBetween(filter.getMinAmount(), filter.getMaxAmount()))
                .and(BookingSpecification.guestsBetween(filter.getMinGuests(), filter.getMaxGuests()));

        List<BookingEntity> bookingEntities = bookingRepository.findAll(spec);

        return bookingEntities.stream()
                .map(bookingMapper::toDomain)
                .toList();
    }
}
