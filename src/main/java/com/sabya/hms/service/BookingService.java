package com.sabya.hms.service;

import com.sabya.hms.domain.Booking;
import com.sabya.hms.entity.BookingEntity;
import com.sabya.hms.mapper.BookingMapper;
import com.sabya.hms.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final BookingMapper bookingMapper;

    private final BookingRepository bookingRepository;

    public BookingService(BookingMapper bookingMapper, BookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }

    public Booking create(Booking booking) {
        BookingEntity bookingEntity = bookingMapper.toEntity(booking);
        BookingEntity savedEntity = bookingRepository.save(bookingEntity);
        syncBookingRoomDates(savedEntity);
        return bookingMapper.toDomain(savedEntity);
    }

    private void syncBookingRoomDates(BookingEntity savedEntity) {
        savedEntity.getBookingRooms().
                forEach(br -> {
                    br.setCheckInDate(savedEntity.getCheckInDate());
                    br.setCheckOutDate(savedEntity.getCheckOutDate());
                    br.setStatus(savedEntity.getStatus());
                });
    }

    public Booking get(long id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        return bookingMapper.toDomain(bookingEntity);
    }

    public Booking update(Booking booking) {
        BookingEntity bookingEntity = bookingMapper.toEntity(booking);
        BookingEntity savedEntity = bookingRepository.save(bookingEntity);
        syncBookingRoomDates(savedEntity);
        return bookingMapper.toDomain(bookingEntity);
    }

    public Booking delete(long id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(bookingEntity);
        return bookingMapper.toDomain(bookingEntity);
    }
}
