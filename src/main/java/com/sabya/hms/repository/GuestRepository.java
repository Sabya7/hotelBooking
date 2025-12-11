package com.sabya.hms.repository;

import com.sabya.hms.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
}
