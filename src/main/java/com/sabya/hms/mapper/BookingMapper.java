package com.sabya.hms.mapper;

import com.sabya.hms.domain.*;
import com.sabya.hms.dto.BookingRequest;
import com.sabya.hms.dto.BookingResponse;
import com.sabya.hms.entity.BookingEntity;
import com.sabya.hms.entity.BookingRoomEntity;
import com.sabya.hms.service.GuestService;
import com.sabya.hms.service.HotelService;
import com.sabya.hms.service.RoomService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    private final GuestMapper guestMapper;

    private final BookingRoomMapper bookingRoomMapper;
    private final GuestService guestService;
    private final RoomService roomService;
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    public BookingMapper(GuestMapper guestMapper, BookingRoomMapper bookingRoomMapper, GuestService guestService, RoomService roomService, HotelService hotelService, HotelMapper hotelMapper) {
        this.guestMapper = guestMapper;
        this.bookingRoomMapper = bookingRoomMapper;
        this.guestService = guestService;
        this.roomService = roomService;
        this.hotelService = hotelService;
        this.hotelMapper = hotelMapper;
    }

    public Booking toDomain(BookingEntity bookingEntity) {
        if(bookingEntity == null)
            return null;

        Booking booking = Booking.builder()
                .bookedBy(guestMapper.toDomain(bookingEntity.getBookedBy()))
                .id(bookingEntity.getId())
                .checkInDate(bookingEntity.getCheckInDate())
                .checkOutDate(bookingEntity.getCheckOutDate())
                .numberOfGuests(bookingEntity.getNumberOfGuests())
                .hotel(hotelMapper.toDomain(bookingEntity.getHotel()))
                .status(bookingEntity.getStatus())
                .totalAmount(bookingEntity.getTotalAmount())
                .createdAt(bookingEntity.getCreatedAt())
                .updatedAt(bookingEntity.getUpdatedAt())
                .build();

        List<BookingRoom> bookingRooms = bookingEntity.getBookingRooms().stream()
                .map(bookingRoomMapper::toDomain)
                .peek(br -> br.setBooking(booking))
                .toList();

        booking.setBookingRooms(bookingRooms);
        return booking;
    }

    public BookingEntity toEntity(Booking booking) {
        if(booking == null)
            return null;


        BookingEntity bookingEntity = BookingEntity.builder()
                .bookedBy(guestMapper.toEntity(booking.getBookedBy()))
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .status(booking.getStatus())
                .numberOfGuests(booking.getNumberOfGuests())
                .totalAmount(booking.getTotalAmount())
                .hotel(hotelMapper.toEntity(booking.getHotel()))
                .updatedAt(booking.getUpdatedAt())
                .createdAt(booking.getCreatedAt())
                .build();

        if(booking.getId() > 0)
            bookingEntity.setId(booking.getId());

        List<BookingRoomEntity> bookingRoomEntities =
                booking.getBookingRooms().stream()
                        .map(bookingRoomMapper::toEntity)
                        .peek(brEntity -> {
                            brEntity.setBooking(bookingEntity);
                            brEntity.setCheckInDate(bookingEntity.getCheckInDate());
                            brEntity.setCheckOutDate(bookingEntity.getCheckOutDate());
                            brEntity.setStatus(booking.getStatus());
                        })
                        .toList();

        bookingEntity.setBookingRooms(bookingRoomEntities);

        return bookingEntity;
    }

    public Booking toDomain(BookingRequest bookingRequest) {
        if(bookingRequest == null)
            return null;

        Guest guest = guestService.get(bookingRequest.getGuestId());
        Hotel hotel = hotelService.get(bookingRequest.getHotelId());
        List<BookingRoom> bookingRooms = bookingRequest.getRoomIds().stream()
                .map(roomId -> {
                    Room room = roomService.read(roomId);
                    return BookingRoom.builder().room(room).build();
                })
                .toList();

        BookingStatus status = bookingRequest.getStatus() != null ? bookingRequest.getStatus() : BookingStatus.CONFIRMED;

        return Booking.builder()
                .numberOfGuests(bookingRequest.getNumberOfGuests())
                .bookedBy(guest)
                .bookingRooms(bookingRooms)
                .status(status)
                .hotel(hotel)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .totalAmount(bookingRequest.getTotalAmount())
                .build();
    }

    public BookingResponse toResponseDTO(Booking booking) {
        if(booking == null)
            return null;

        List<Long> roomIds = booking.getBookingRooms().stream().map(br -> br.getRoom().getId()).toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .totalAmount(booking.getTotalAmount())
                .roomIds(roomIds)
                .hotelId(booking.getHotel().getId())
                .status(booking.getStatus())
                .guestId(booking.getBookedBy().getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .updatedAt(booking.getUpdatedAt())
                .createdAt(booking.getCreatedAt())
                .numberOfGuests(booking.getNumberOfGuests())
                .build();
    }
}
