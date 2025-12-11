package com.sabya.hms.repository;

import com.sabya.hms.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Long> {

    List<RoomTypeEntity> findByHotelId(long hotelId);
}
