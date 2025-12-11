package com.sabya.hms.service;

import com.sabya.hms.domain.RoomType;
import com.sabya.hms.entity.RoomTypeEntity;
import com.sabya.hms.mapper.RoomTypeMapper;
import com.sabya.hms.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeService(RoomTypeRepository roomTypeRepository, RoomTypeMapper roomTypeMapper) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomTypeMapper = roomTypeMapper;
    }

    public RoomType create(RoomType roomType) {
        RoomTypeEntity entity = roomTypeMapper.toEntity(roomType);
        RoomTypeEntity savedEntity = roomTypeRepository.save(entity);
        return roomTypeMapper.toDomain(savedEntity);
    }

    public RoomType update(RoomType roomType) {
        RoomTypeEntity entity = roomTypeMapper.toEntity(roomType);
        RoomTypeEntity savedEntity = roomTypeRepository.save(entity);
        return roomTypeMapper.toDomain(savedEntity);
    }

    public RoomType read(long id) {
        RoomTypeEntity entity = roomTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("RoomType not found"));
        return roomTypeMapper.toDomain(entity);
    }

    public RoomType delete(long id) {
        RoomTypeEntity entity = roomTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("RoomType not found"));
        roomTypeRepository.delete(entity);
        return roomTypeMapper.toDomain(entity);
    }

    public List<RoomType> findByHotelId(long hotelId) {
        return roomTypeRepository.findByHotelId(hotelId).stream().map(roomTypeMapper::toDomain).collect(Collectors.toList());
    }
}
