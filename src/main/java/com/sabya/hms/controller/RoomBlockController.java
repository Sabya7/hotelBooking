package com.sabya.hms.controller;

import com.sabya.hms.domain.RoomBlock;
import com.sabya.hms.dto.RoomBlockRequest;
import com.sabya.hms.dto.RoomBlockResponse;
import com.sabya.hms.mapper.RoomBlockMapper;
import com.sabya.hms.service.RoomBlockService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roomBlock")
public class RoomBlockController {

    private final RoomBlockMapper roomBlockMapper;

    private final RoomBlockService roomBlockService;

    public RoomBlockController(RoomBlockMapper roomBlockMapper, RoomBlockService roomBlockService) {
        this.roomBlockMapper = roomBlockMapper;
        this.roomBlockService = roomBlockService;
    }

    @PostMapping
    public RoomBlockResponse create(@RequestBody RoomBlockRequest request) {
        RoomBlock roomBlock = roomBlockMapper.toDomain(request);

        RoomBlock savedRoomBlock = roomBlockService.create(roomBlock);

        return roomBlockMapper.toResponseDTO(savedRoomBlock);
    }

    @GetMapping("/{id}")
    public RoomBlockResponse get(@PathVariable long id) {
        RoomBlock roomBlock = roomBlockService.get(id);
        return roomBlockMapper.toResponseDTO(roomBlock);
    }

    @PutMapping("/{id}")
    public RoomBlockResponse update(@PathVariable long id, @RequestBody RoomBlockRequest roomBlockRequest) {
        RoomBlock roomBlock = roomBlockMapper.toDomain(roomBlockRequest);
        roomBlock.setId(id);
        RoomBlock updatedRoomBlock = roomBlockService.update(roomBlock);
        return roomBlockMapper.toResponseDTO(updatedRoomBlock);
    }

    @DeleteMapping("/{id}")
    public RoomBlockResponse delete(@PathVariable long id) {
        RoomBlock roomBlock = roomBlockService.delete(id);
        return roomBlockMapper.toResponseDTO(roomBlock);
    }
}
