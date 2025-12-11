package com.sabya.hms.service;

import com.sabya.hms.domain.Guest;
import com.sabya.hms.entity.GuestEntity;
import com.sabya.hms.mapper.GuestMapper;
import com.sabya.hms.repository.GuestRepository;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    private final GuestMapper guestMapper;

    private final GuestRepository guestRepository;

    public GuestService(GuestMapper guestMapper, GuestRepository guestRepository) {
        this.guestMapper = guestMapper;
        this.guestRepository = guestRepository;
    }


    public Guest create(Guest guest) {
        GuestEntity guestEntity = guestMapper.toEntity(guest);
        GuestEntity savedEntity = guestRepository.save(guestEntity);
        return guestMapper.toDomain(savedEntity);
    }

    public Guest get(long id) {
        GuestEntity guestEntity = guestRepository.findById(id).orElseThrow(() -> new RuntimeException("Guest does not exist"));
        return guestMapper.toDomain(guestEntity);
    }

    public Guest update(Guest guest) {
        GuestEntity guestEntity = guestMapper.toEntity(guest);
        GuestEntity savedEntity = guestRepository.save(guestEntity);
        return guestMapper.toDomain(savedEntity);
    }

    public Guest delete(long id) {
        GuestEntity guestEntity = guestRepository.findById(id).orElseThrow(() -> new RuntimeException("Guest does not exist"));
        guestRepository.delete(guestEntity);
        return guestMapper.toDomain(guestEntity);
    }
}
