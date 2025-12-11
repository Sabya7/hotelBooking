package com.sabya.hms.controller;

import com.sabya.hms.domain.Guest;
import com.sabya.hms.dto.GuestRequest;
import com.sabya.hms.dto.GuestResponse;
import com.sabya.hms.mapper.GuestMapper;
import com.sabya.hms.service.GuestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/guest")
public class GuestController {

    private final GuestService guestService;

    private final GuestMapper guestMapper;

    public GuestController(GuestService guestService, GuestMapper guestMapper) {
        this.guestService = guestService;
        this.guestMapper = guestMapper;
    }


    @PostMapping
    public GuestResponse create(@RequestBody GuestRequest guestRequest) {
        Guest guest = guestMapper.toDomain(guestRequest);
        Guest savedGuest = guestService.create(guest);
        return guestMapper.toResponseDTO(savedGuest);
    }

    @GetMapping("/{id}")
    public GuestResponse get(@PathVariable long id) {
        Guest guest = guestService.get(id);
        return guestMapper.toResponseDTO(guest);
    }

    @PutMapping("/{id}")
    public GuestResponse update(@PathVariable long id, @RequestBody GuestRequest guestRequest) {
        Guest guest = guestMapper.toDomain(guestRequest);
        guest.setId(id);
        Guest updatedGuest = guestService.update(guest);
        return guestMapper.toResponseDTO(updatedGuest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        guestService.delete(id);
    }
}
