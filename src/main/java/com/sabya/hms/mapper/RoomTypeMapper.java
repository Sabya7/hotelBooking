package com.sabya.hms.mapper;

import com.sabya.hms.domain.RoomType;
import com.sabya.hms.dto.RoomTypeRequest;
import com.sabya.hms.dto.RoomTypeResponse;
import com.sabya.hms.entity.RoomTypeEntity;
import com.sabya.hms.service.HotelService;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeMapper {

    private final HotelMapper hotelMapper;
    private final HotelService hotelService;

    public RoomTypeMapper(HotelMapper hotelMapper, HotelService hotelService) {
        this.hotelMapper = hotelMapper;
        this.hotelService = hotelService;
    }

    public RoomType toDomain(RoomTypeEntity roomTypeEntity) {
        if(roomTypeEntity ==  null)
            return null;

        RoomType.RoomTypeBuilder builder = RoomType.builder()
                .id(roomTypeEntity.getId())
                .baseRate(roomTypeEntity.getBaseRate())
                .maxOccupancy(roomTypeEntity.getMaxOccupancy())
                .name(roomTypeEntity.getName())
                .hotel(hotelMapper.toDomain(roomTypeEntity.getHotel()))
                .createdAt(roomTypeEntity.getCreatedAt())
                .updatedAt(roomTypeEntity.getUpdatedAt());

        return builder.build();
    }

    public RoomTypeEntity toEntity(RoomType roomType) {
        if(roomType == null)
            return null;

        RoomTypeEntity.RoomTypeEntityBuilder builder = RoomTypeEntity.builder()
                .baseRate(roomType.getBaseRate())
                .maxOccupancy(roomType.getMaxOccupancy())
                .name(roomType.getName())
                .hotel(hotelMapper.toEntity(roomType.getHotel()));

        if(roomType.getId() > 0)
            builder.id(roomType.getId());

        return builder.build();
    }

    public RoomTypeResponse toResponseDTO(RoomType roomType) {
        if(roomType == null)
            return null;

        RoomTypeResponse.RoomTypeResponseBuilder builder = RoomTypeResponse.builder()
                .id(roomType.getId())
                .baseRate(roomType.getBaseRate())
                .createdAt(roomType.getCreatedAt())
                .updatedAt(roomType.getUpdatedAt())
                .hotelId(roomType.getHotel().getId())
                .maxOccupancy(roomType.getMaxOccupancy())
                .name(roomType.getName());

        return builder.build();
    }

    public RoomType toDomain(RoomTypeRequest roomTypeRequest) {
        if(roomTypeRequest == null)
            return null;

        RoomType.RoomTypeBuilder builder = RoomType.builder()
                .name(roomTypeRequest.getName())
                .maxOccupancy(roomTypeRequest.getMaxOccupancy())
                .hotel(hotelService.get(roomTypeRequest.getHotelId()))
                .baseRate(roomTypeRequest.getBaseRate());

        return builder.build();
    }
}
