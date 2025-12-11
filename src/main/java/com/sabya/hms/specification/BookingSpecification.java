package com.sabya.hms.specification;

import com.sabya.hms.domain.BookingStatus;
import com.sabya.hms.entity.BookingEntity;
import com.sabya.hms.entity.BookingRoomEntity;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingSpecification {

    public static Specification<BookingEntity> hasHotel(Long hotelId) {
        return (root, query, criteriaBuilder) -> hotelId == null ? null : criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
    }

    public static Specification<BookingEntity> hasStatus(BookingStatus status) {
        return (root, query, criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<BookingEntity> hasStatuses(List<BookingStatus> statuses) {
        return (root, query, criteriaBuilder) -> statuses == null || statuses.isEmpty() ? null :
                root.get("status").in(statuses);
    }

    public static Specification<BookingEntity> hasGuest(Long guestId) {
        return (root, query, criteriaBuilder) -> guestId == null ? null : criteriaBuilder.equal(root.get("bookedBy").get("id"), guestId);
    }

    public static Specification<BookingEntity> hasRoom(Long roomId) {
        return (root, query, criteriaBuilder) -> {
            if(roomId == null)
                return null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<BookingRoomEntity> br = subquery.from(BookingRoomEntity.class);
            subquery.select(criteriaBuilder.literal(1L))
                    .where(
                            criteriaBuilder.equal(br.get("booking").get("id"), root.get("id")),
                            criteriaBuilder.equal(br.get("room").get("id"), roomId)
                    );

            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<BookingEntity> checkInBetween(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {
            if(start == null && end == null)
                return null;

            Path<LocalDate> checkInDate = root.get("checkInDate");

            if(start != null && end != null) {
                return criteriaBuilder.between(checkInDate, start, end);
            }
            else if(start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(checkInDate, start);
            }
            else {
                return criteriaBuilder.lessThanOrEqualTo(checkInDate, end);
            }
        };
    }

    public static Specification<BookingEntity> checkOutBetween(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {
            if(start == null && end == null)
                return null;

            Path<LocalDate> checkOutDate = root.get("checkOutDate");

            if(start != null && end != null) {
                return criteriaBuilder.between(checkOutDate, start, end);
            }
            else if(start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(checkOutDate, start);
            }
            else {
                return criteriaBuilder.lessThanOrEqualTo(checkOutDate, end);
            }
        };
    }

    public static Specification<BookingEntity> overlapsWith(LocalDate start, LocalDate end) {
        return (root, query, criteriaBuilder) -> {
            if(start == null || end == null)
                return null;

            Path<LocalDate> checkInDate = root.get("checkInDate");
            Path<LocalDate> checkOutDate = root.get("checkOutDate");

            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(checkInDate, end),
                    criteriaBuilder.greaterThanOrEqualTo(checkOutDate, start)
            );
        };
    }

    public static Specification<BookingEntity> amountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if(min == null && max == null)
                return null;

            Path<BigDecimal> totalAmount = root.get("totalAmount");

            if(min != null && max != null) {
                return criteriaBuilder.between(totalAmount, min, max);
            }
            else if(min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(totalAmount, min);
            }
            else {
                return criteriaBuilder.lessThanOrEqualTo(totalAmount, max);
            }
        };
    }

    public static Specification<BookingEntity> guestsBetween(Integer min, Integer max) {
        return (root, query, criteriaBuilder) -> {
            if(min == null && max == null)
                return null;

            Path<Integer> numberOfGuests = root.get("numberOfGuests");

            if(min != null && max != null) {
                return criteriaBuilder.between(numberOfGuests, min, max);
            }
            else if(min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(numberOfGuests, min);
            }
            else {
                return criteriaBuilder.lessThanOrEqualTo(numberOfGuests, max);
            }
        };
    }
}
