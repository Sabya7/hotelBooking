package com.sabya.hms.service;

import com.sabya.hms.domain.Room;
import com.sabya.hms.domain.RoomStatus;
import com.sabya.hms.dto.RoomAvailabilityFilter;
import com.sabya.hms.dto.RoomAvailabilityQueryResult;
import com.sabya.hms.dto.RoomAvailabilityResponse;
import com.sabya.hms.entity.RoomEntity;
import com.sabya.hms.mapper.RoomMapper;
import com.sabya.hms.repository.RoomRepository;
import com.sabya.hms.specification.RoomSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomQueryService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public RoomQueryService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Transactional(readOnly = true)
    public List<Room> findAvailableRoomsByHotelAndDateRange(long hotelId, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal minPrice, BigDecimal maxPrice) {
        List<RoomEntity> availableRoomsByHotelAndDateRange = roomRepository.findAvailableRoomsByHotelAndDateRange(hotelId, checkInDate, checkOutDate, minPrice, maxPrice);

        return availableRoomsByHotelAndDateRange.stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Room> findAvailableRooms(RoomAvailabilityFilter filter) {
        Specification<RoomEntity> spec = Specification
                .where(RoomSpecification.hasHotel(filter.getHotelId()))
                .and(RoomSpecification.hasMinOccupancy(filter.getMinOccupancy()))
                .and(RoomSpecification.hasRoomType(filter.getRoomTypeId()))
                .and(RoomSpecification.priceBetween(filter.getMinPrice(), filter.getMaxPrice()))
                .and(RoomSpecification.availableBetween(filter.getCheckIn(), filter.getCheckOut()));

        List<RoomEntity> roomEntities = roomRepository.findAll(spec);

        return roomEntities.stream().map(roomMapper::toDomain).toList();
    }

    @Transactional(readOnly = true)
    public RoomAvailabilityResponse checkRoomAvailability(long hotelId, long roomId, LocalDate checkIn, LocalDate checkOut) {
        RoomAvailabilityQueryResult roomAvailabilityQueryResult = roomRepository.checkAvailabilityWithConflicts(hotelId, roomId, checkIn, checkOut);
        RoomAvailabilityResponse.RoomAvailabilityResponseBuilder builder = RoomAvailabilityResponse.builder()
                .roomId(roomId)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .available(roomAvailabilityQueryResult.getAvailable());

        if(!roomAvailabilityQueryResult.getAvailable() && roomAvailabilityQueryResult.getConflictType() != null) {
            builder.reason(roomAvailabilityQueryResult.getConflictType());
            builder.conflictDetails(
                    RoomAvailabilityResponse.ConflictDetails.builder()
                            .type(roomAvailabilityQueryResult.getConflictType())
                            .blockId(roomAvailabilityQueryResult.getBlockId())
                            .bookingId(roomAvailabilityQueryResult.getBookingId())
                            .conflictStart(roomAvailabilityQueryResult.getConflictStart())
                            .conflictEnd(roomAvailabilityQueryResult.getConflictEnd())
                            .build()
            );
        }
        else
            builder.reason("AVAILABLE");

        return builder.build();
    }

    @Transactional(readOnly = true)
    public List<Room> findRoomsByHotelAndStatus(long hotelId, RoomStatus status) {
        return roomRepository.findByHotelIdAndStatusOrderByRoomNumberAsc(hotelId, status).stream()
                .map(roomMapper::toDomain)
                .toList();
    }
}
