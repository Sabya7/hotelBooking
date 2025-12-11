package com.sabya.hms.mapper;

import com.sabya.hms.domain.Hotel;
import com.sabya.hms.dto.HotelRequest;
import com.sabya.hms.dto.HotelResponse;
import com.sabya.hms.entity.HotelEntity;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public HotelEntity toEntity(Hotel hotel) {
        if(hotel == null)
            return null;

        HotelEntity.HotelEntityBuilder builder = HotelEntity.builder()
                .name(hotel.getName())
                .address(hotel.getAddress());

        if(hotel.getId() > 0) {
            builder.id(hotel.getId());
        }

        return builder.build();
    }

    public Hotel toDomain(HotelEntity hotelEntity) {
        if(hotelEntity == null)
            return null;

        Hotel.HotelBuilder builder = Hotel.builder()
                .name(hotelEntity.getName())
                .address(hotelEntity.getAddress())
                .createdAt(hotelEntity.getCreatedAt())
                .updatedAt(hotelEntity.getUpdatedAt())
                .id(hotelEntity.getId());

        return builder.build();
    }

    public Hotel toDomain(HotelRequest request) {
        if(request == null)
            return null;

        Hotel.HotelBuilder builder = Hotel.builder()
                .name(request.getName())
                .address(request.getAddress());

        return builder.build();
    }

    public HotelResponse toResponseDTO(Hotel hotel) {
        if(hotel == null)
            return null;

        HotelResponse.HotelResponseBuilder builder = HotelResponse.builder()
                .name(hotel.getName())
                .address(hotel.getAddress())
                .createdAt(hotel.getCreatedAt())
                .updatedAt(hotel.getUpdatedAt())
                .id(hotel.getId());

        return builder.build();
    }
}
