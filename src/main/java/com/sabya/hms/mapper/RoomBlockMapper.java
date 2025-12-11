package com.sabya.hms.mapper;

import com.sabya.hms.domain.Room;
import com.sabya.hms.domain.RoomBlock;
import com.sabya.hms.dto.RoomBlockRequest;
import com.sabya.hms.dto.RoomBlockResponse;
import com.sabya.hms.entity.RoomBlockEntity;
import com.sabya.hms.service.RoomService;
import org.springframework.stereotype.Component;

@Component
public class RoomBlockMapper {

    private final RoomMapper roomMapper;
    private final RoomService roomService;

    public RoomBlockMapper(RoomMapper roomMapper, RoomService roomService) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
    }

    public RoomBlock toDomain(RoomBlockEntity roomBlockEntity) {
        if(roomBlockEntity == null)
            return null;
        return RoomBlock.builder()
                .room(roomMapper.toDomain(roomBlockEntity.getRoom()))
                .id(roomBlockEntity.getId())
                .reason(roomBlockEntity.getReason())
                .blockType(roomBlockEntity.getBlockType())
                .createdAt(roomBlockEntity.getCreatedAt())
                .updatedAt(roomBlockEntity.getUpdatedAt())
                .endDate(roomBlockEntity.getEndDate())
                .startDate(roomBlockEntity.getStartDate())
                .build();
    }

    public RoomBlockEntity toEntity(RoomBlock roomBlock) {
        if(roomBlock == null)
            return null;

        RoomBlockEntity.RoomBlockEntityBuilder builder = RoomBlockEntity.builder()
                .blockType(roomBlock.getBlockType())
                .createdAt(roomBlock.getCreatedAt())
                .updatedAt(roomBlock.getUpdatedAt())
                .reason(roomBlock.getReason())
                .room(roomMapper.toEntity(roomBlock.getRoom()))
                .startDate(roomBlock.getStartDate())
                .endDate(roomBlock.getEndDate());

        if(roomBlock.getId() > 0)
            builder.id(roomBlock.getId());

        return builder.build();
    }

    public RoomBlock toDomain(RoomBlockRequest roomBlockRequest) {
        if (roomBlockRequest == null)
            return null;

        Room room = roomService.read(roomBlockRequest.getRoomId());

        return RoomBlock.builder()
                .room(room)
                .startDate(roomBlockRequest.getStartDate())
                .endDate(roomBlockRequest.getEndDate())
                .blockType(roomBlockRequest.getBlockType())
                .reason(roomBlockRequest.getReason())
                .build();
    }

    public RoomBlockResponse toResponseDTO(RoomBlock roomBlock) {
        if(roomBlock == null)
            return null;

        return RoomBlockResponse.builder()
                .roomId(roomBlock.getRoom().getId())
                .id(roomBlock.getId())
                .reason(roomBlock.getReason())
                .blockType(roomBlock.getBlockType())
                .endDate(roomBlock.getEndDate())
                .startDate(roomBlock.getStartDate())
                .createdAt(roomBlock.getCreatedAt())
                .updatedAt(roomBlock.getUpdatedAt())
                .build();
    }
}
