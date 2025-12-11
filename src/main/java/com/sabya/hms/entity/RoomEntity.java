package com.sabya.hms.entity;

import com.sabya.hms.domain.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
