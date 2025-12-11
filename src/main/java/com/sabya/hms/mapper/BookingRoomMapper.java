package com.sabya.hms.mapper;

import com.sabya.hms.domain.BookingRoom;
import com.sabya.hms.entity.BookingRoomEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingRoomMapper {

    private final RoomMapper roomMapper;

    public BookingRoomMapper(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    public BookingRoom toDomain(BookingRoomEntity bookingRoomEntity) {
        if(bookingRoomEntity == null)
            return null;
        return BookingRoom.builder()
                .room(roomMapper.toDomain(bookingRoomEntity.getRoom()))
                .build();
    }

    public BookingRoomEntity toEntity(BookingRoom bookingRoom) {
        if(bookingRoom == null)
            return null;

        return BookingRoomEntity.builder().room(roomMapper.toEntity(bookingRoom.getRoom())).build();
    }
}
