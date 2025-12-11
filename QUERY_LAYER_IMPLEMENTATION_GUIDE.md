# HMS Query Layer Implementation Guide

## 📋 Table of Contents
1. [Overview](#overview)
2. [Architecture Approach](#architecture-approach)
3. [Room Availability & Search](#1-room-availability--search)
4. [Booking Queries](#2-booking-queries)
5. [Guest Queries](#3-guest-queries)
6. [Business Operations](#4-business-operations)
7. [Reporting & Analytics](#5-reporting--analytics)
8. [Implementation Order](#implementation-order)
9. [Code Patterns & Examples](#code-patterns--examples)

---

## Overview

### Current State
- ✅ **Commands (CRUD)**: Complete
- ❌ **Queries**: Missing
- ❌ **Business Operations**: Missing
- ❌ **Reporting**: Missing

### Goal
Build a complete query layer following CQRS principles with separate query services.

---

## Architecture Approach

### Pattern: Separate Query Services (CQRS Style)

```
Service Layer Structure:
├── BookingService (Commands) ✅ Existing
├── BookingQueryService (Queries) ❌ To Create
├── RoomService (Commands) ✅ Existing
├── RoomQueryService (Queries) ❌ To Create
├── GuestService (Commands) ✅ Existing
└── GuestQueryService (Queries) ❌ To Create
```

### Key Principles
1. **Read-Only Transactions**: All query services use `@Transactional(readOnly = true)`
2. **Separate Services**: Query services separate from command services
3. **Optimized Queries**: Custom repository methods for performance
4. **DTOs for Responses**: Use existing Response DTOs or create query-specific DTOs

---

## 1. Room Availability & Search

### Priority: 🔴 HIGH

### Queries to Implement

#### 1.1 Find Available Rooms for Date Range
**Use Case**: Guest searches for rooms: "Show me available rooms for Jan 15-18"

**Repository Method** (`RoomRepository.java`):
```java
@Query("""
    SELECT r FROM RoomEntity r 
    WHERE r.status = 'AVAILABLE'
    AND r.id NOT IN (
        SELECT br.room.id FROM BookingRoomEntity br
        JOIN br.booking b
        WHERE b.status IN ('CONFIRMED', 'CHECKED_IN')
        AND (b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)
    )
    """)
List<RoomEntity> findAvailableRooms(
    @Param("checkIn") LocalDate checkIn,
    @Param("checkOut") LocalDate checkOut
);
```

**Service Method** (`RoomQueryService.java`):
```java
public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
    // Implementation
}
```

**Controller Endpoint** (`RoomController.java`):
```java
@GetMapping("/available")
public List<RoomResponse> getAvailableRooms(
    @RequestParam LocalDate checkIn,
    @RequestParam LocalDate checkOut
)
```

---

#### 1.2 Find Rooms by Type and Availability
**Use Case**: "Show me available Deluxe rooms for Jan 15-18"

**Repository Method**:
```java
@Query("""
    SELECT r FROM RoomEntity r 
    WHERE r.roomType.id = :roomTypeId
    AND r.status = 'AVAILABLE'
    AND r.id NOT IN (
        SELECT br.room.id FROM BookingRoomEntity br
        JOIN br.booking b
        WHERE b.status IN ('CONFIRMED', 'CHECKED_IN')
        AND (b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)
    )
    """)
List<RoomEntity> findAvailableRoomsByType(
    @Param("roomTypeId") long roomTypeId,
    @Param("checkIn") LocalDate checkIn,
    @Param("checkOut") LocalDate checkOut
);
```

**Service Method**:
```java
public List<Room> findAvailableRoomsByType(
    long roomTypeId, 
    LocalDate checkIn, 
    LocalDate checkOut
)
```

**Controller Endpoint**:
```java
@GetMapping("/available/by-type")
public List<RoomResponse> getAvailableRoomsByType(
    @RequestParam long roomTypeId,
    @RequestParam LocalDate checkIn,
    @RequestParam LocalDate checkOut
)
```

---

#### 1.3 Find Rooms by Status
**Use Case**: Staff checks room status

**Repository Method**:
```java
List<RoomEntity> findByStatus(RoomStatus status);
List<RoomEntity> findByHotelIdAndStatus(long hotelId, RoomStatus status);
```

**Service Method**:
```java
public List<Room> findRoomsByStatus(RoomStatus status)
public List<Room> findRoomsByHotelAndStatus(long hotelId, RoomStatus status)
```

**Controller Endpoint**:
```java
@GetMapping("/status/{status}")
public List<RoomResponse> getRoomsByStatus(@PathVariable RoomStatus status)
```

---

#### 1.4 Check if Room is Available for Specific Dates
**Use Case**: System validates room availability during booking

**Repository Method**:
```java
@Query("""
    SELECT COUNT(br) = 0 FROM BookingRoomEntity br
    JOIN br.booking b
    WHERE br.room.id = :roomId
    AND b.status IN ('CONFIRMED', 'CHECKED_IN')
    AND (b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)
    """)
boolean isRoomAvailable(
    @Param("roomId") long roomId,
    @Param("checkIn") LocalDate checkIn,
    @Param("checkOut") LocalDate checkOut
);
```

**Service Method**:
```java
public boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut)
```

**Controller Endpoint**:
```java
@GetMapping("/{roomId}/availability")
public boolean checkRoomAvailability(
    @PathVariable long roomId,
    @RequestParam LocalDate checkIn,
    @RequestParam LocalDate checkOut
)
```

---

#### 1.5 Find Rooms by Hotel
**Use Case**: "Show all rooms for this hotel"

**Repository Method** (Already exists pattern):
```java
List<RoomEntity> findByHotelId(long hotelId);
```

**Service Method**:
```java
public List<Room> findRoomsByHotel(long hotelId)
```

**Controller Endpoint**:
```java
@GetMapping("/hotel/{hotelId}")
public List<RoomResponse> getRoomsByHotel(@PathVariable long hotelId)
```

---

#### 1.6 Get Room Occupancy Calendar (Optional - Advanced)
**Use Case**: Management view of room occupancy over time

**Repository Method**:
```java
@Query("""
    SELECT b.checkInDate, b.checkOutDate, COUNT(br.room.id) 
    FROM BookingEntity b
    JOIN b.bookingRooms br
    WHERE br.room.id = :roomId
    AND b.status IN ('CONFIRMED', 'CHECKED_IN')
    AND b.checkInDate BETWEEN :startDate AND :endDate
    GROUP BY b.checkInDate, b.checkOutDate
    """)
List<Object[]> getRoomOccupancyCalendar(
    @Param("roomId") long roomId,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate
);
```

**Note**: This requires a custom DTO for the response.

---

### Files to Create/Modify

1. **Create**: `RoomQueryService.java`
2. **Modify**: `RoomRepository.java` (add query methods)
3. **Modify**: `RoomController.java` (add query endpoints)

---

## 2. Booking Queries

### Priority: 🔴 HIGH

### Queries to Implement

#### 2.1 Find Bookings by Guest
**Use Case**: Guest: "Show my booking history"

**Repository Method** (`BookingRepository.java`):
```java
List<BookingEntity> findByBookedByIdOrderByCreatedAtDesc(long guestId);
```

**Service Method** (`BookingQueryService.java`):
```java
public List<Booking> findBookingsByGuest(long guestId)
```

**Controller Endpoint** (`BookingController.java`):
```java
@GetMapping("/guest/{guestId}")
public List<BookingResponse> getBookingsByGuest(@PathVariable long guestId)
```

---

#### 2.2 Find Bookings by Date Range
**Use Case**: Management: "Show all bookings for January"

**Repository Method**:
```java
List<BookingEntity> findByCheckInDateBetween(
    LocalDate startDate, 
    LocalDate endDate
);
```

**Service Method**:
```java
public List<Booking> findBookingsByDateRange(
    LocalDate startDate, 
    LocalDate endDate
)
```

**Controller Endpoint**:
```java
@GetMapping("/date-range")
public List<BookingResponse> getBookingsByDateRange(
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate
)
```

---

#### 2.3 Find Bookings by Status
**Use Case**: "Show all confirmed bookings"

**Repository Method**:
```java
List<BookingEntity> findByStatusOrderByCheckInDateAsc(BookingStatus status);
```

**Service Method**:
```java
public List<Booking> findBookingsByStatus(BookingStatus status)
```

**Controller Endpoint**:
```java
@GetMapping("/status/{status}")
public List<BookingResponse> getBookingsByStatus(@PathVariable BookingStatus status)
```

---

#### 2.4 Find Active Bookings
**Use Case**: Management: "Show all active bookings"

**Repository Method**:
```java
List<BookingEntity> findByStatusInOrderByCheckInDateAsc(
    List<BookingStatus> statuses
);
// Use: Arrays.asList(BookingStatus.CONFIRMED, BookingStatus.CHECKED_IN)
```

**Service Method**:
```java
public List<Booking> findActiveBookings()
```

**Controller Endpoint**:
```java
@GetMapping("/active")
public List<BookingResponse> getActiveBookings()
```

---

#### 2.5 Find Bookings for a Specific Room
**Use Case**: "Show all bookings for Room 101"

**Repository Method**:
```java
@Query("""
    SELECT DISTINCT b FROM BookingEntity b
    JOIN b.bookingRooms br
    WHERE br.room.id = :roomId
    ORDER BY b.checkInDate DESC
    """)
List<BookingEntity> findByRoomId(@Param("roomId") long roomId);
```

**Service Method**:
```java
public List<Booking> findBookingsByRoom(long roomId)
```

**Controller Endpoint**:
```java
@GetMapping("/room/{roomId}")
public List<BookingResponse> getBookingsByRoom(@PathVariable long roomId)
```

---

#### 2.6 Find Upcoming Check-Ins for Today
**Use Case**: Front desk: "Show all check-ins today"

**Repository Method**:
```java
List<BookingEntity> findByCheckInDateAndStatus(
    LocalDate date, 
    BookingStatus status
);
// Use: findByCheckInDateAndStatus(today, BookingStatus.CONFIRMED)
```

**Service Method**:
```java
public List<Booking> findCheckInsToday()
```

**Controller Endpoint**:
```java
@GetMapping("/check-ins/today")
public List<BookingResponse> getCheckInsToday()
```

---

#### 2.7 Find Upcoming Check-Outs for Today
**Use Case**: Front desk: "Show all check-outs today"

**Repository Method**:
```java
List<BookingEntity> findByCheckOutDateAndStatus(
    LocalDate date, 
    BookingStatus status
);
// Use: findByCheckOutDateAndStatus(today, BookingStatus.CHECKED_IN)
```

**Service Method**:
```java
public List<Booking> findCheckOutsToday()
```

**Controller Endpoint**:
```java
@GetMapping("/check-outs/today")
public List<BookingResponse> getCheckOutsToday()
```

---

#### 2.8 Find Overdue Check-Outs (Optional)
**Use Case**: "Show guests who should have checked out"

**Repository Method**:
```java
@Query("""
    SELECT b FROM BookingEntity b
    WHERE b.checkOutDate < :today
    AND b.status = 'CHECKED_IN'
    ORDER BY b.checkOutDate ASC
    """)
List<BookingEntity> findOverdueCheckOuts(@Param("today") LocalDate today);
```

**Service Method**:
```java
public List<Booking> findOverdueCheckOuts()
```

**Controller Endpoint**:
```java
@GetMapping("/check-outs/overdue")
public List<BookingResponse> getOverdueCheckOuts()
```

---

#### 2.9 Get Booking History for a Guest
**Use Case**: Customer service: "Show guest's past bookings"

**Note**: This is same as 2.1, but you can add sorting/filtering options.

**Service Method** (Enhanced):
```java
public List<Booking> getGuestBookingHistory(long guestId, BookingStatus status)
```

---

### Files to Create/Modify

1. **Create**: `BookingQueryService.java`
2. **Modify**: `BookingRepository.java` (add query methods)
3. **Modify**: `BookingController.java` (add query endpoints)

---

## 3. Guest Queries

### Priority: 🟡 MEDIUM

### Queries to Implement

#### 3.1 Find Guest by Email
**Use Case**: Staff searches for guest by email

**Repository Method** (`GuestRepository.java`):
```java
Optional<GuestEntity> findByEmail(String email);
```

**Service Method** (`GuestQueryService.java`):
```java
public Optional<Guest> findGuestByEmail(String email)
```

**Controller Endpoint** (`GuestController.java`):
```java
@GetMapping("/search/email")
public GuestResponse getGuestByEmail(@RequestParam String email)
```

---

#### 3.2 Find Guests by Name (Partial Match)
**Use Case**: Staff searches for guest by name

**Repository Method**:
```java
List<GuestEntity> findByNameContainingIgnoreCase(String name);
```

**Service Method**:
```java
public List<Guest> findGuestsByName(String name)
```

**Controller Endpoint**:
```java
@GetMapping("/search/name")
public List<GuestResponse> searchGuestsByName(@RequestParam String name)
```

---

#### 3.3 Get Guest Booking History
**Use Case**: Customer service: "Show guest's past bookings"

**Note**: This uses BookingQueryService method from section 2.1

**Service Method** (in GuestQueryService):
```java
public List<Booking> getGuestBookingHistory(long guestId)
// Delegates to BookingQueryService.findBookingsByGuest(guestId)
```

**Controller Endpoint**:
```java
@GetMapping("/{guestId}/bookings")
public List<BookingResponse> getGuestBookings(@PathVariable long guestId)
```

---

#### 3.4 Find Frequent Guests (Optional - Advanced)
**Use Case**: "Show guests with most bookings"

**Repository Method**:
```java
@Query("""
    SELECT g, COUNT(b.id) as bookingCount 
    FROM GuestEntity g
    JOIN BookingEntity b ON b.bookedBy.id = g.id
    GROUP BY g.id
    ORDER BY bookingCount DESC
    """)
List<Object[]> findFrequentGuests(@Param("limit") int limit);
```

**Note**: Requires custom DTO for response.

---

#### 3.5 Get Guest Statistics (Optional - Advanced)
**Use Case**: "Show guest's total bookings and spending"

**Service Method**:
```java
public GuestStatistics getGuestStatistics(long guestId)
// Returns: totalBookings, totalSpent, averageBookingValue, lastBookingDate
```

**Note**: Requires custom DTO `GuestStatistics.java`

---

### Files to Create/Modify

1. **Create**: `GuestQueryService.java`
2. **Modify**: `GuestRepository.java` (add query methods)
3. **Modify**: `GuestController.java` (add query endpoints)

---

## 4. Business Operations

### Priority: 🔴 HIGH

### Operations to Implement

#### 4.1 Check-In Process
**Use Case**: Guest checks in → room becomes OCCUPIED

**Operation** (Add to `BookingService.java`):
```java
public Booking checkIn(long bookingId) {
    // 1. Find booking
    // 2. Validate: status must be CONFIRMED
    // 3. Update booking status to CHECKED_IN
    // 4. Update all rooms in booking to OCCUPIED
    // 5. Save and return
}
```

**Controller Endpoint** (`BookingController.java`):
```java
@PostMapping("/{id}/check-in")
public BookingResponse checkIn(@PathVariable long id)
```

**Business Rules**:
- Booking status must be CONFIRMED
- Cannot check in before check-in date
- All rooms in booking become OCCUPIED

---

#### 4.2 Check-Out Process
**Use Case**: Guest checks out → room becomes DIRTY

**Operation** (Add to `BookingService.java`):
```java
public Booking checkOut(long bookingId) {
    // 1. Find booking
    // 2. Validate: status must be CHECKED_IN
    // 3. Update booking status to CHECKED_OUT
    // 4. Update all rooms in booking to DIRTY
    // 5. Save and return
}
```

**Controller Endpoint**:
```java
@PostMapping("/{id}/check-out")
public BookingResponse checkOut(@PathVariable long id)
```

**Business Rules**:
- Booking status must be CHECKED_IN
- All rooms in booking become DIRTY (for housekeeping)

---

#### 4.3 Cancel Booking
**Use Case**: Guest cancels → booking becomes CANCELLED, rooms freed

**Operation** (Add to `BookingService.java`):
```java
public Booking cancelBooking(long bookingId) {
    // 1. Find booking
    // 2. Validate: can only cancel CONFIRMED bookings
    // 3. Update booking status to CANCELLED
    // 4. Rooms automatically freed (no booking = available)
    // 5. Save and return
}
```

**Controller Endpoint**:
```java
@PostMapping("/{id}/cancel")
public BookingResponse cancelBooking(@PathVariable long id)
```

**Business Rules**:
- Can only cancel CONFIRMED bookings
- Cannot cancel CHECKED_IN or CHECKED_OUT bookings

---

#### 4.4 Validate Booking
**Use Case**: System validates room availability during booking creation

**Operation** (Add to `BookingService.java` or create `BookingValidator.java`):
```java
public void validateBooking(CreateBookingCommand command) {
    // 1. Check if all rooms are available for date range
    // 2. Check date validity (checkOut > checkIn)
    // 3. Check guest exists
    // 4. Check rooms exist
    // 5. Throw exception if validation fails
}
```

**Use in** `BookingService.create()`:
```java
public Booking create(Booking booking) {
    validateBooking(booking); // Add validation
    // ... rest of create logic
}
```

---

#### 4.5 Calculate Booking Total (Optional - Advanced)
**Use Case**: Calculate total from room rates automatically

**Operation** (Add to `BookingService.java`):
```java
public BigDecimal calculateBookingTotal(
    List<Long> roomIds, 
    LocalDate checkIn, 
    LocalDate checkOut
) {
    // 1. Get all rooms
    // 2. Get base rate from room type
    // 3. Calculate nights (checkOut - checkIn)
    // 4. Calculate: rate * nights for each room
    // 5. Sum all room totals
    // 6. Return total
}
```

**Use in** `BookingMapper.toDomain(BookingRequest)`:
```java
// Instead of requiring totalAmount in request,
// calculate it automatically
BigDecimal totalAmount = bookingService.calculateBookingTotal(
    request.getRoomIds(),
    request.getCheckInDate(),
    request.getCheckOutDate()
);
```

---

### Files to Modify

1. **Modify**: `BookingService.java` (add business operations)
2. **Modify**: `BookingController.java` (add operation endpoints)
3. **Optional**: Create `BookingValidator.java` for validation logic

---

## 5. Reporting & Analytics

### Priority: 🟡 MEDIUM

### Reports to Implement

#### 5.1 Revenue Report (by Date Range)
**Use Case**: Management: "Show revenue for last month"

**Service Method** (Create `ReportService.java`):
```java
public RevenueReport getRevenueReport(LocalDate startDate, LocalDate endDate) {
    // 1. Query bookings in date range with CHECKED_OUT status
    // 2. Sum totalAmount
    // 3. Group by room type (optional)
    // 4. Return RevenueReport DTO
}
```

**DTO** (Create `RevenueReport.java`):
```java
public class RevenueReport {
    private BigDecimal totalRevenue;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RevenueByRoomType> revenueByRoomType;
    // ... other fields
}
```

**Controller Endpoint** (Create `ReportController.java`):
```java
@GetMapping("/revenue")
public RevenueReport getRevenueReport(
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate
)
```

---

#### 5.2 Occupancy Rate (by Date Range)
**Use Case**: Operations: "What's our occupancy rate?"

**Service Method**:
```java
public OccupancyReport getOccupancyRate(LocalDate startDate, LocalDate endDate) {
    // 1. Get total rooms
    // 2. Get booked rooms for each day in range
    // 3. Calculate: (booked rooms / total rooms) * 100
    // 4. Return OccupancyReport DTO
}
```

**DTO** (Create `OccupancyReport.java`):
```java
public class OccupancyReport {
    private double occupancyRate; // percentage
    private int totalRooms;
    private int bookedRooms;
    private LocalDate startDate;
    private LocalDate endDate;
}
```

---

#### 5.3 Popular Room Types
**Use Case**: "Which room types are most booked?"

**Service Method**:
```java
public List<RoomTypePopularity> getPopularRoomTypes(LocalDate startDate, LocalDate endDate) {
    // 1. Query bookings in date range
    // 2. Count bookings by room type
    // 3. Order by count DESC
    // 4. Return list
}
```

---

#### 5.4 Booking Trends (Daily/Monthly)
**Use Case**: "Show booking trends over time"

**Service Method**:
```java
public List<BookingTrend> getBookingTrends(
    LocalDate startDate, 
    LocalDate endDate,
    String period // "DAILY" or "MONTHLY"
) {
    // 1. Group bookings by period
    // 2. Count bookings per period
    // 3. Return trends
}
```

---

#### 5.5 Guest Statistics
**Use Case**: "Show guest statistics"

**Service Method**:
```java
public GuestStatisticsReport getGuestStatistics() {
    // 1. Total guests
    // 2. New guests (this month)
    // 3. Repeat guests
    // 4. Average bookings per guest
}
```

---

#### 5.6 Room Utilization
**Use Case**: "Show room utilization report"

**Service Method**:
```java
public List<RoomUtilization> getRoomUtilization(LocalDate startDate, LocalDate endDate) {
    // 1. For each room, calculate utilization percentage
    // 2. Utilization = (days booked / total days) * 100
    // 3. Return list sorted by utilization
}
```

---

### Files to Create

1. **Create**: `ReportService.java`
2. **Create**: `ReportController.java`
3. **Create**: Report DTOs:
   - `RevenueReport.java`
   - `OccupancyReport.java`
   - `RoomTypePopularity.java`
   - `BookingTrend.java`
   - `GuestStatisticsReport.java`
   - `RoomUtilization.java`

---

## Implementation Order

### Week 1: Core Queries (Days 1-5)

**Day 1-2: Room Availability**
- ✅ RoomRepository: `findAvailableRooms()`
- ✅ RoomQueryService: Create service
- ✅ RoomController: Add `/available` endpoint
- ✅ Test manually

**Day 3: Booking Queries (Basic)**
- ✅ BookingRepository: Add basic query methods
- ✅ BookingQueryService: Create service
- ✅ BookingController: Add query endpoints

**Day 4: Guest Search**
- ✅ GuestRepository: Add search methods
- ✅ GuestQueryService: Create service
- ✅ GuestController: Add search endpoints

**Day 5: Business Operations**
- ✅ BookingService: Add `checkIn()`, `checkOut()`, `cancelBooking()`
- ✅ BookingController: Add operation endpoints

---

### Week 2: Advanced Features (Days 6-10)

**Day 6-7: Advanced Booking Queries**
- ✅ Add remaining booking queries
- ✅ Add validation logic

**Day 8-9: Reporting**
- ✅ Create ReportService
- ✅ Create ReportController
- ✅ Create report DTOs
- ✅ Implement basic reports

**Day 10: Polish & Review**
- ✅ Test all endpoints
- ✅ Refactor if needed
- ✅ Document APIs

---

## Code Patterns & Examples

### Pattern 1: Query Service Structure

```java
@Service
@Transactional(readOnly = true) // ✅ Important: Read-only
public class RoomQueryService {
    
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    
    public RoomQueryService(
        RoomRepository roomRepository,
        RoomMapper roomMapper
    ) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }
    
    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<RoomEntity> entities = roomRepository.findAvailableRooms(checkIn, checkOut);
        return entities.stream()
            .map(roomMapper::toDomain)
            .toList();
    }
}
```

---

### Pattern 2: Repository Query Method

```java
@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    
    // Simple query method
    List<BookingEntity> findByBookedByIdOrderByCreatedAtDesc(long guestId);
    
    // Custom query with @Query
    @Query("""
        SELECT b FROM BookingEntity b
        WHERE b.status = :status
        AND b.checkInDate BETWEEN :startDate AND :endDate
        ORDER BY b.checkInDate ASC
        """)
    List<BookingEntity> findByStatusAndDateRange(
        @Param("status") BookingStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
```

---

### Pattern 3: Controller Query Endpoint

```java
@RestController
@RequestMapping("api/booking")
public class BookingController {
    
    private final BookingQueryService bookingQueryService;
    private final BookingMapper bookingMapper;
    
    @GetMapping("/guest/{guestId}")
    public List<BookingResponse> getBookingsByGuest(@PathVariable long guestId) {
        List<Booking> bookings = bookingQueryService.findBookingsByGuest(guestId);
        return bookings.stream()
            .map(bookingMapper::toResponseDTO)
            .toList();
    }
}
```

---

### Pattern 4: Business Operation

```java
@Service
@Transactional // ✅ Write operation - needs transaction
public class BookingService {
    
    public Booking checkIn(long bookingId) {
        Booking booking = get(bookingId);
        
        // Business validation
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED bookings can be checked in");
        }
        
        // Update booking status
        booking.setStatus(BookingStatus.CHECKED_IN);
        
        // Update room statuses
        booking.getBookingRooms().forEach(br -> {
            br.getRoom().setStatus(RoomStatus.OCCUPIED);
        });
        
        return update(booking);
    }
}
```

---

## Important Notes

### 1. Transaction Management
- **Query Services**: Use `@Transactional(readOnly = true)`
- **Command Services**: Use `@Transactional` (default)

### 2. Exception Handling
- Use appropriate exceptions (you're skipping for now, but keep in mind)
- Validate inputs before queries

### 3. Performance
- Use `@Query` for complex queries
- Consider pagination for large result sets
- Add indexes if needed (you can add later)

### 4. DTOs
- Reuse existing Response DTOs where possible
- Create new DTOs only when needed (e.g., reports)

---

## Testing Checklist

After implementation, manually test:

- [ ] Room availability query works
- [ ] Booking queries return correct data
- [ ] Guest search finds guests
- [ ] Check-in updates statuses correctly
- [ ] Check-out updates statuses correctly
- [ ] Cancel booking works
- [ ] Reports calculate correctly

---

## Next Steps

1. **Start with Room Availability** (Priority 1)
2. **Then Booking Queries** (Priority 2)
3. **Then Guest Search** (Priority 3)
4. **Then Business Operations** (Priority 4)
5. **Finally Reporting** (Priority 5)

---

## Summary

This guide provides:
- ✅ All queries to implement
- ✅ Repository methods needed
- ✅ Service methods structure
- ✅ Controller endpoints
- ✅ Code patterns and examples
- ✅ Implementation order

**Now go build it!** 🚀

Remember: You're building this yourself to achieve mastery. Take your time, understand each pattern, and implement one feature at a time.

Good luck! 💪


