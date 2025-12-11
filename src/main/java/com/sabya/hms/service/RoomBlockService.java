package com.sabya.hms.service;

import com.sabya.hms.domain.RoomBlock;
import com.sabya.hms.entity.RoomBlockEntity;
import com.sabya.hms.mapper.RoomBlockMapper;
import com.sabya.hms.repository.RoomBlockRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomBlockService {

    private final RoomBlockRepository roomBlockRepository;

    private final RoomBlockMapper roomBlockMapper;

    public RoomBlockService(RoomBlockRepository roomBlockRepository, RoomBlockMapper roomBlockMapper) {
        this.roomBlockRepository = roomBlockRepository;
        this.roomBlockMapper = roomBlockMapper;
    }


    public RoomBlock create(RoomBlock roomBlock) {
        RoomBlockEntity roomBlockEntity = roomBlockMapper.toEntity(roomBlock);
        RoomBlockEntity savedEntity = roomBlockRepository.save(roomBlockEntity);
        return roomBlockMapper.toDomain(savedEntity);
    }

    public RoomBlock update(RoomBlock roomBlock) {
        RoomBlockEntity roomBlockEntity = roomBlockMapper.toEntity(roomBlock);
        RoomBlockEntity savedEntity = roomBlockRepository.save(roomBlockEntity);
        return roomBlockMapper.toDomain(savedEntity);
    }

    public RoomBlock get(long id) {
        RoomBlockEntity roomBlockEntity = roomBlockRepository.findById(id).orElseThrow(() -> new RuntimeException("Room block not found"));
        return roomBlockMapper.toDomain(roomBlockEntity);
    }

    public RoomBlock delete(long id) {
        RoomBlockEntity roomBlockEntity = roomBlockRepository.findById(id).orElseThrow(() -> new RuntimeException("Room block not found"));
        roomBlockRepository.delete(roomBlockEntity);
        return roomBlockMapper.toDomain(roomBlockEntity);
    }
}
