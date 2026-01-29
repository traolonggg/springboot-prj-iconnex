package com.hotel.roommanagement.repository;

import com.hotel.roommanagement.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RoomType entity
 * 
 * Provides data access methods for room type operations
 * including custom queries for business logic.
 */
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

    /**
     * Find room type by name (case-insensitive)
     */
    Optional<RoomType> findByNameIgnoreCase(String name);

    /**
     * Check if room type exists by name (excluding specific ID)
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /**
     * Find all active room types
     */
    List<RoomType> findByIsActiveTrue();

    /**
     * Find active room types with pagination
     */
    Page<RoomType> findByIsActiveTrue(Pageable pageable);

    /**
     * Find room types by price range
     */
    @Query("SELECT rt FROM RoomType rt WHERE rt.basePrice BETWEEN :minPrice AND :maxPrice AND rt.isActive = true")
    List<RoomType> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find room types by maximum occupancy
     */
    List<RoomType> findByMaxOccupancyGreaterThanEqualAndIsActiveTrue(Integer minOccupancy);

    /**
     * Find room types with available rooms
     */
    @Query("SELECT DISTINCT rt FROM RoomType rt " +
           "JOIN rt.rooms r " +
           "WHERE r.status = 'AVAILABLE' AND r.isActive = true AND rt.isActive = true")
    List<RoomType> findRoomTypesWithAvailableRooms();

    /**
     * Get room type statistics
     */
    @Query("SELECT rt.id, rt.name, COUNT(r.id) as roomCount, " +
           "COUNT(CASE WHEN r.status = 'AVAILABLE' AND r.isActive = true THEN 1 END) as availableCount " +
           "FROM RoomType rt LEFT JOIN rt.rooms r " +
           "WHERE rt.isActive = true " +
           "GROUP BY rt.id, rt.name")
    List<Object[]> getRoomTypeStatistics();

    /**
     * Search room types by name or description
     */
    @Query("SELECT rt FROM RoomType rt WHERE " +
           "(LOWER(rt.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(rt.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "rt.isActive = true")
    Page<RoomType> searchRoomTypes(@Param("searchTerm") String searchTerm, Pageable pageable);
}