package com.sabya.hms.mapper;

import com.sabya.hms.domain.Room;
import com.sabya.hms.dto.RoomRequest;
import com.sabya.hms.dto.RoomResponse;
import com.sabya.hms.entity.RoomEntity;
import com.sabya.hms.service.HotelService;
import com.sabya.hms.service.RoomTypeService;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    private final HotelMapper hotelMapper;

    private final RoomTypeMapper roomTypeMapper;

    private final HotelService hotelService;

    private final RoomTypeService roomTypeService;

    public RoomMapper(HotelMapper hotelMapper, RoomTypeMapper roomTypeMapper, HotelService hotelService, RoomTypeService roomTypeService) {
        this.hotelMapper = hotelMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.hotelService = hotelService;
        this.roomTypeService = roomTypeService;
    }

    public Room toDomain(RoomEntity roomEntity) {
        if(roomEntity == null)
            return null;

        Room.RoomBuilder builder = Room.builder()
                .id(roomEntity.getId())
                .hotel(hotelMapper.toDomain(roomEntity.getHotel()))
                .roomType(roomTypeMapper.toDomain(roomEntity.getRoomType()))
                .roomNumber(roomEntity.getRoomNumber())
                .createdAt(roomEntity.getCreatedAt())
                .status(roomEntity.getStatus())
                .updatedAt(roomEntity.getUpdatedAt());

        return builder.build();
    }

    public RoomEntity toEntity(Room room) {
        if(room == null)
            return null;

        RoomEntity.RoomEntityBuilder builder = RoomEntity.builder()
                .hotel(hotelMapper.toEntity(room.getHotel()))
                .roomType(roomTypeMapper.toEntity(room.getRoomType()))
                .roomNumber(room.getRoomNumber())
                .status(room.getStatus());

        if(room.getId() > 0)
            builder.id(room.getId());

        return builder.build();
    }

    public Room toDomain(RoomRequest roomRequest) {
        if(roomRequest == null)
            return null;

        Room.RoomBuilder builder = Room.builder()
                .roomNumber(roomRequest.getRoomNumber())
                .status(roomRequest.getStatus())
                .hotel(hotelService.get(roomRequest.getHotelId()))
                .roomType(roomTypeService.read(roomRequest.getRoomTypeId()));

        return builder.build();
    }

    public RoomResponse toResponseDTO(Room room) {
        if(room == null)
            return null;

        RoomResponse.RoomResponseBuilder builder = RoomResponse.builder()
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .hotelId(room.getHotel().getId())
                .roomTypeId(room.getRoomType().getId())
                .status(room.getStatus())
                .id(room.getId())
                .roomNumber(room.getRoomNumber());

        return builder.build();
    }
}
