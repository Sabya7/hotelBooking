package com.sabya.hms.controller;


import com.sabya.hms.domain.Hotel;
import com.sabya.hms.dto.HotelRequest;
import com.sabya.hms.dto.HotelResponse;
import com.sabya.hms.mapper.HotelMapper;
import com.sabya.hms.service.HotelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/hotels")
public class HotelController {

    private final HotelService hotelService;

    private final HotelMapper hotelMapper;

    public HotelController(HotelService hotelService, HotelMapper hotelMapper) {
        this.hotelService = hotelService;
        this.hotelMapper = hotelMapper;
    }

    @PostMapping
    public HotelResponse create(@RequestBody HotelRequest request) {
        Hotel hotel = hotelMapper.toDomain(request);
        Hotel savedHotel = hotelService.create(hotel);
        return hotelMapper.toResponseDTO(savedHotel);
    }

    @GetMapping("/{id}")
    public HotelResponse get(@PathVariable long id) {
        Hotel hotel = hotelService.get(id);
        return hotelMapper.toResponseDTO(hotel);
    }

    @PutMapping("/{id}")
    public HotelResponse put(@PathVariable long id, @RequestBody HotelRequest request) {
        Hotel hotel = hotelMapper.toDomain(request);
        hotel.setId(id);
        Hotel updatedHotel = hotelService.update(hotel);
        return hotelMapper.toResponseDTO(updatedHotel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        hotelService.delete(id);
    }
}
