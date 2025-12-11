package com.sabya.hms.service;

import com.sabya.hms.domain.Room;
import com.sabya.hms.entity.RoomEntity;
import com.sabya.hms.mapper.RoomMapper;
import com.sabya.hms.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public Room create(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);
        RoomEntity savedRoom = roomRepository.save(roomEntity);
        return roomMapper.toDomain(savedRoom);
    }

    public Room read(long id) {
        RoomEntity roomEntity = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not available"));
        return roomMapper.toDomain(roomEntity);
    }

    public Room update(Room room) {
        RoomEntity roomEntity = roomMapper.toEntity(room);
        RoomEntity savedEntity = roomRepository.save(roomEntity);
        return roomMapper.toDomain(savedEntity);
    }

    public Room delete(long id) {
        RoomEntity roomEntity = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room doesn't exist"));
        roomRepository.deleteById(id);
        return roomMapper.toDomain(roomEntity);
    }
}
