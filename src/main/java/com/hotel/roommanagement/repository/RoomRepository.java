package com.hotel.roommanagement.repository;

import com.hotel.roommanagement.entity.Room;
import com.hotel.roommanagement.enums.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Room entity
 * 
 * Provides data access methods for room operations
 * including custom queries for business logic.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Find room by room number
     */
    Optional<Room> findByRoomNumber(String roomNumber);

    /**
     * Check if room number exists (excluding specific ID)
     */
    boolean existsByRoomNumberAndIdNot(String roomNumber, Long id);

    /**
     * Find all active rooms
     */
    List<Room> findByIsActiveTrue();

    /**
     * Find active rooms with pagination
     */
    Page<Room> findByIsActiveTrue(Pageable pageable);

    /**
     * Find rooms by status
     */
    List<Room> findByStatusAndIsActiveTrue(RoomStatus status);

    /**
     * Find rooms by room type
     */
    List<Room> findByRoomTypeIdAndIsActiveTrue(Long roomTypeId);

    /**
     * Find rooms by floor
     */
    List<Room> findByFloorAndIsActiveTrue(Integer floor);

    /**
     * Find available rooms
     */
    @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND r.isActive = true")
    List<Room> findAvailableRooms();

    /**
     * Find available rooms by room type
     */
    @Query("SELECT r FROM Room r WHERE r.roomTypeId = :roomTypeId AND r.status = 'AVAILABLE' AND r.isActive = true")
    List<Room> findAvailableRoomsByType(@Param("roomTypeId") Long roomTypeId);

    /**
     * Find rooms with filters
     */
    @Query("SELECT r FROM Room r JOIN r.roomType rt WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:roomTypeId IS NULL OR r.roomTypeId = :roomTypeId) AND " +
           "(:floor IS NULL OR r.floor = :floor) AND " +
           "(:viewType IS NULL OR LOWER(r.viewType) LIKE LOWER(CONCAT('%', :viewType, '%'))) AND " +
           "(:hasBalcony IS NULL OR r.hasBalcony = :hasBalcony) AND " +
           "(:minPrice IS NULL OR rt.basePrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR rt.basePrice <= :maxPrice) AND " +
           "(:isActive IS NULL OR r.isActive = :isActive)")
    Page<Room> findRoomsWithFilters(
        @Param("status") RoomStatus status,
        @Param("roomTypeId") Long roomTypeId,
        @Param("floor") Integer floor,
        @Param("viewType") String viewType,
        @Param("hasBalcony") Boolean hasBalcony,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("isActive") Boolean isActive,
        Pageable pageable
    );

    /**
     * Get room statistics by status
     */
    @Query("SELECT r.status, COUNT(r) FROM Room r WHERE r.isActive = true GROUP BY r.status")
    List<Object[]> getRoomStatisticsByStatus();

    /**
     * Get room statistics by room type
     */
    @Query("SELECT rt.name, COUNT(r) FROM Room r JOIN r.roomType rt WHERE r.isActive = true GROUP BY rt.name")
    List<Object[]> getRoomStatisticsByType();

    /**
     * Get room statistics by floor
     */
    @Query("SELECT r.floor, COUNT(r) FROM Room r WHERE r.isActive = true GROUP BY r.floor ORDER BY r.floor")
    List<Object[]> getRoomStatisticsByFloor();

    /**
     * Find rooms needing maintenance (last maintenance > 30 days ago or never)
     */
    @Query("SELECT r FROM Room r WHERE r.isActive = true AND " +
           "(r.lastMaintenance IS NULL OR r.lastMaintenance < :cutoffDate)")
    List<Room> findRoomsNeedingMaintenance(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Count rooms by status
     */
    long countByStatusAndIsActiveTrue(RoomStatus status);

    /**
     * Count total active rooms
     */
    long countByIsActiveTrue();

    /**
     * Search rooms by room number or notes
     */
    @Query("SELECT r FROM Room r WHERE " +
           "(LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "r.isActive = true")
    Page<Room> searchRooms(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find rooms by view type
     */
    List<Room> findByViewTypeIgnoreCaseAndIsActiveTrue(String viewType);

    /**
     * Find rooms with balcony
     */
    List<Room> findByHasBalconyTrueAndIsActiveTrue();

    /**
     * Get occupancy rate
     */
    @Query("SELECT " +
           "COUNT(CASE WHEN r.status = 'OCCUPIED' THEN 1 END) * 100.0 / COUNT(r) " +
           "FROM Room r WHERE r.isActive = true")
    Double getOccupancyRate();
}