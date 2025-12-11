package com.sabya.hms.specification;

import com.sabya.hms.domain.BookingStatus;
import com.sabya.hms.entity.BookingRoomEntity;
import com.sabya.hms.entity.RoomBlockEntity;
import com.sabya.hms.entity.RoomEntity;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RoomSpecification {

    public static Specification<RoomEntity> hasHotel(Long hotelId) {
        return (root, query, criteriaBuilder) -> hotelId == null ? null : criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
    }

    public static Specification<RoomEntity> hasRoomType(Long roomTypeId) {
        return (root, query, criteriaBuilder) -> roomTypeId == null ? null : criteriaBuilder.equal(root.get("roomType").get("id"), roomTypeId);
    }

    public static Specification<RoomEntity> hasMinOccupancy(Integer minOccupancy) {
        return (root, query, criteriaBuilder) -> minOccupancy == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("roomType").get("maxOccupancy"), minOccupancy);
    }

    public static Specification<RoomEntity> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if(minPrice == null && maxPrice == null)
                return null;

            Path<BigDecimal> baseRate = root.get("roomType").get("baseRate");

            if(minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(baseRate, minPrice, maxPrice);
            }
            else if (minPrice != null)
                return criteriaBuilder.greaterThanOrEqualTo(baseRate, minPrice);
            else
                return criteriaBuilder.lessThanOrEqualTo(baseRate, maxPrice);
        };
    }

    public static Specification<RoomEntity> availableBetween(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, criteriaBuilder) -> {
            if(checkIn == null || checkOut == null)
                return null;

            Subquery<Long> bookingSubQuery = query.subquery(Long.class);
            Root<BookingRoomEntity> br = bookingSubQuery.from(BookingRoomEntity.class);
            bookingSubQuery.select(criteriaBuilder.literal(1L))
                    .where(
                            criteriaBuilder.equal(br.get("room").get("id"), root.get("id")),
                            criteriaBuilder.in(br.get("status")).value(List.of(BookingStatus.CHECKED_IN, BookingStatus.CONFIRMED)),
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(br.get("checkInDate"), checkOut),
                                    criteriaBuilder.greaterThanOrEqualTo(br.get("checkOutDate"), checkIn)
                            )
                    );

            Subquery<Long> blockingSubQuery = query.subquery(Long.class);
            Root<RoomBlockEntity> rb = blockingSubQuery.from(RoomBlockEntity.class);
            blockingSubQuery.select(criteriaBuilder.literal(1L))
                    .where(
                            criteriaBuilder.equal(rb.get("room").get("id"), root.get("id")),
                            criteriaBuilder.and(
                                    criteriaBuilder.lessThanOrEqualTo(rb.get("startDate"), checkOut),
                                    criteriaBuilder.greaterThanOrEqualTo(rb.get("endDate"), checkIn)
                            )
                    );

            return criteriaBuilder.and(
                    criteriaBuilder.not(criteriaBuilder.exists(bookingSubQuery)),
                    criteriaBuilder.not(criteriaBuilder.exists(blockingSubQuery))
            );
        };
    }
}
