package com.sabya.hms.repository;

import com.sabya.hms.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

    List<BookingEntity> findByHotelIdAndBookedByIdOrderByCreatedAtDesc(long hotelId, long guestId);
}
