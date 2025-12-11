package com.sabya.hms.controller;

import com.sabya.hms.domain.RoomType;
import com.sabya.hms.dto.RoomTypeRequest;
import com.sabya.hms.dto.RoomTypeResponse;
import com.sabya.hms.mapper.RoomTypeMapper;
import com.sabya.hms.service.RoomTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/roomType")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeController(RoomTypeService roomTypeService, RoomTypeMapper roomTypeMapper) {
        this.roomTypeService = roomTypeService;
        this.roomTypeMapper = roomTypeMapper;
    }

    @PostMapping
    public RoomTypeResponse create(@RequestBody RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeMapper.toDomain(roomTypeRequest);
        RoomType roomTypeSaved = roomTypeService.create(roomType);
        return roomTypeMapper.toResponseDTO(roomTypeSaved);
    }

    @GetMapping("/{id}")
    public RoomTypeResponse read(@PathVariable long id) {
        RoomType roomType = roomTypeService.read(id);
        return roomTypeMapper.toResponseDTO(roomType);
    }

    @PutMapping("/{id}")
    public RoomTypeResponse put(@PathVariable long id, @RequestBody RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeMapper.toDomain(roomTypeRequest);
        roomType.setId(id);
        RoomType updatedRoomType = roomTypeService.update(roomType);
        return roomTypeMapper.toResponseDTO(updatedRoomType);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        roomTypeService.delete(id);
    }

    @GetMapping("/hotel/{hotelId}")
    public List<RoomTypeResponse> getAllRoomTypesByHotel(@PathVariable long hotelId) {
        return roomTypeService.findByHotelId(hotelId).stream().map(roomTypeMapper::toResponseDTO).collect(Collectors.toList());
    }
}
