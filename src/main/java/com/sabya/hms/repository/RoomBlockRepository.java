package com.sabya.hms.repository;

import com.sabya.hms.entity.RoomBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomBlockRepository extends JpaRepository<RoomBlockEntity, Long> {
}
