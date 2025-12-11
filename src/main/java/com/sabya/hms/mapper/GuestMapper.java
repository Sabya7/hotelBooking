package com.sabya.hms.mapper;

import com.sabya.hms.domain.Guest;
import com.sabya.hms.dto.GuestRequest;
import com.sabya.hms.dto.GuestResponse;
import com.sabya.hms.entity.GuestEntity;
import com.sabya.hms.repository.GuestRepository;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    private final GuestRepository guestRepository;

    public GuestMapper(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest toDomain(GuestEntity guestEntity) {
        if(guestEntity == null)
            return null;

        Guest.GuestBuilder builder = Guest.builder()
                .id(guestEntity.getId())
                .address(guestEntity.getAddress())
                .name(guestEntity.getName())
                .email(guestEntity.getEmail())
                .updatedAt(guestEntity.getUpdatedAt())
                .createdAt(guestEntity.getCreatedAt());

        return builder.build();
    }

    public GuestEntity toEntity(Guest guest) {
        if(guest == null)
            return null;

        GuestEntity.GuestEntityBuilder builder = GuestEntity.builder()
                .createdAt(guest.getCreatedAt())
                .updatedAt(guest.getUpdatedAt())
                .email(guest.getEmail())
                .address(guest.getAddress())
                .name(guest.getName());

        if(guest.getId() > 0)
            builder.id(guest.getId());

        return builder.build();
    }

    public GuestResponse toResponseDTO(Guest guest) {
         if(guest == null)
             return null;

         GuestResponse.GuestResponseBuilder builder = GuestResponse.builder()
                 .address(guest.getAddress())
                 .email(guest.getEmail())
                 .updatedAt(guest.getUpdatedAt())
                 .createdAt(guest.getCreatedAt())
                 .id(guest.getId())
                 .name(guest.getName());

         return builder.build();
    }

    public Guest toDomain(GuestRequest guestRequest) {
        if(guestRequest == null)
            return null;

        Guest.GuestBuilder guestBuilder = Guest.builder()
                .address(guestRequest.getAddress())
                .email(guestRequest.getEmail())
                .name(guestRequest.getName());

        return guestBuilder.build();
    }
}
