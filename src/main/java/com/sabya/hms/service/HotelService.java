package com.sabya.hms.service;

import com.sabya.hms.domain.Hotel;
import com.sabya.hms.entity.HotelEntity;
import com.sabya.hms.mapper.HotelMapper;
import com.sabya.hms.repository.HotelRepository;
import org.springframework.stereotype.Service;


@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    public Hotel create(Hotel hotel) {
        HotelEntity entity = hotelMapper.toEntity(hotel);
        HotelEntity savedEntity = hotelRepository.save(entity);
        return hotelMapper.toDomain(savedEntity);
    }

    public Hotel update(Hotel hotel) {
        HotelEntity entity = hotelMapper.toEntity(hotel);
        HotelEntity savedEntity = hotelRepository.save(entity);
        return hotelMapper.toDomain(savedEntity);
    }

    public Hotel get(long id) {
        HotelEntity entity = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel Not Found"));
        return hotelMapper.toDomain(entity);
    }

    public void delete(long id) {
        HotelEntity entity = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel Not Found"));
        Hotel hotel = hotelMapper.toDomain(entity);
        hotelRepository.delete(entity);
    }
}
