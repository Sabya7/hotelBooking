package com.sabya.hms.repository;

import com.sabya.hms.domain.RoomStatus;
import com.sabya.hms.dto.RoomAvailabilityQueryResult;
import com.sabya.hms.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long>, JpaSpecificationExecutor<RoomEntity> {

    @Query("""
        Select r from RoomEntity r 
        WHERE r.hotel.id = :hotelId
        AND (:minPrice is NULL OR r.roomType.baseRate >= :minPrice)
        AND (:maxPrice is NULL OR r.roomType.baseRate <= :maxPrice)
        AND NOT EXISTS (
            SELECT 1 from BookingRoomEntity br
            where br.room.id = r.id
            and br.status in (com.sabya.hms.domain.BookingStatus.CONFIRMED, com.sabya.hms.domain.BookingStatus.CHECKED_IN)
            AND (br.checkInDate <= :checkOut and br.checkOutDate >= :checkIn)
        )
        AND not EXISTS (
            SELECT 1 from RoomBlockEntity rb
            where rb.room.id = r.id
            and rb.startDate <= :checkOut and rb.endDate >= :checkIn
        )
        ORDER BY r.roomNumber
""")
    List<RoomEntity> findAvailableRoomsByHotelAndDateRange(@Param("hotelId") long hotelId,
                                                           @Param("checkIn") LocalDate checkInDate,
                                                           @Param("checkOut") LocalDate checkOutDate,
                                                           @Param("minPrice") BigDecimal minPrice,
                                                           @Param("maxPrice") BigDecimal maxPrice);

    @Query("""
        SELECT
            CASE
                WHEN bookingConflict.id is NOT NULL THEN false
                WHEN blockConflict.id is NOT NULL THEN false
                ELSE true
            END as available,
            CASE
                WHEN bookingConflict.id is not null then 'BOOKING'
                WHEN blockConflict.id is not null then 'BLOCK'
                ELSE null
            END as conflictType,
            bookingConflict.booking.id as bookingId,
            blockConflict.id as blockId,
            COALESCE(bookingConflict.checkInDate, blockConflict.startDate) as conflictStart,
            COALESCE(bookingConflict.checkOutDate, blockConflict.endDate) as conflictEnd
        FROM RoomEntity r
        LEFT JOIN BookingRoomEntity bookingConflict ON bookingConflict.room.id = r.id
            AND bookingConflict.status in ( com.sabya.hms.domain.BookingStatus.CONFIRMED, com.sabya.hms.domain.BookingStatus.CHECKED_IN)
            AND bookingConflict.checkInDate <= :checkOut
            AND bookingConflict.checkOutDate >= :checkIn
        LEFT JOIN RoomBlockEntity blockConflict ON blockConflict.room.id = r.id
            AND blockConflict.startDate <= :checkOut
            AND blockConflict.endDate >= :checkIn
        WHERE r.id = :roomId
        AND r.hotel.id = :hotelId
        ORDER BY
            CASE WHEN bookingConflict.id IS NOT NULL then 1 else 2 end,
            COALESCE(bookingConflict.checkInDate, blockConflict.startDate) ASC
""")
    RoomAvailabilityQueryResult checkAvailabilityWithConflicts(@Param("hotelId") long hotelId,
                                                               @Param("roomId") long roomId,
                                                               @Param("checkIn") LocalDate checkIn,
                                                               @Param("checkOut") LocalDate checkOut);

    List<RoomEntity> findByHotelIdAndStatusOrderByRoomNumberAsc(long hotelId, RoomStatus status);
}
