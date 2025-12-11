package com.sabya.hms.controller;

import com.sabya.hms.domain.Room;
import com.sabya.hms.domain.RoomStatus;
import com.sabya.hms.dto.*;
import com.sabya.hms.mapper.RoomMapper;
import com.sabya.hms.service.RoomQueryService;
import com.sabya.hms.service.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/room")
public class RoomController {

    private final RoomMapper roomMapper;

    private final RoomService roomService;

    private final RoomQueryService roomQueryService;

    public RoomController(RoomMapper roomMapper, RoomService roomService, RoomQueryService roomQueryService) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
        this.roomQueryService = roomQueryService;
    }

    @PostMapping
    public RoomResponse create(@RequestBody RoomRequest roomRequest) {

        Room room = roomMapper.toDomain(roomRequest);
        Room roomSaved = roomService.create(room);
        return roomMapper.toResponseDTO(roomSaved);
    }

    @GetMapping("/{id}")
    public RoomResponse get(@PathVariable long id) {
        Room room = roomService.read(id);
        return roomMapper.toResponseDTO(room);
    }

    @PutMapping("/{id}")
    public RoomResponse put(@PathVariable long id, @RequestBody RoomRequest roomRequest) {
        Room room = roomMapper.toDomain(roomRequest);
        room.setId(id);
        Room updatedRoom = roomService.update(room);
        return roomMapper.toResponseDTO(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        roomService.delete(id);
    }

    @GetMapping("/available")
    public List<RoomResponse> getAvailableRooms(@RequestParam long hotelId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                @RequestParam(required = false) BigDecimal minPrice,
                                                @RequestParam(required = false) BigDecimal maxPrice) {

        List<Room> rooms = roomQueryService.findAvailableRoomsByHotelAndDateRange(hotelId, checkInDate, checkOutDate, minPrice, maxPrice);
        return rooms.stream().map(roomMapper::toResponseDTO).toList();
    }

    @PostMapping("/available/search")
    public List<RoomResponse> getAvailableRooms(@RequestBody RoomAvailabilityFilter filter) {
        return roomQueryService.findAvailableRooms(filter).stream().map(roomMapper::toResponseDTO).toList();
    }

    @GetMapping("/{roomId}/availability")
    public RoomAvailabilityResponse checkAvailability(@RequestParam long hotelId, @PathVariable long roomId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return roomQueryService.checkRoomAvailability(hotelId, roomId, checkIn, checkOut);
    }

    @GetMapping("/status")
    public List<RoomResponse> getRoomsByStatus(
            @RequestParam long hotelId,
            @RequestParam RoomStatus status
            ) {
        return roomQueryService.findRoomsByHotelAndStatus(hotelId, status).stream()
                .map(roomMapper::toResponseDTO)
                .toList();
    }

}
