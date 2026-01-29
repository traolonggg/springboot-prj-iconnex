package com.hotel.roommanagement.service;

import com.hotel.roommanagement.dto.RoomDto;
import com.hotel.roommanagement.entity.Room;
import com.hotel.roommanagement.entity.RoomType;
import com.hotel.roommanagement.enums.RoomStatus;
import com.hotel.roommanagement.exception.ResourceNotFoundException;
import com.hotel.roommanagement.exception.DuplicateResourceException;
import com.hotel.roommanagement.exception.BusinessLogicException;
import com.hotel.roommanagement.mapper.RoomMapper;
import com.hotel.roommanagement.repository.RoomRepository;
import com.hotel.roommanagement.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Room operations
 * 
 * Handles business logic for room management including
 * CRUD operations, validation, and business rules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;

    /**
     * Get all rooms with pagination
     */
    public Page<RoomDto.Response> getAllRooms(Pageable pageable) {
        log.debug("Getting all rooms with pagination: {}", pageable);
        
        Page<Room> rooms = roomRepository.findAll(pageable);
        return rooms.map(roomMapper::toResponse);
    }

    /**
     * Get all active rooms
     */
    public List<RoomDto.ListItem> getActiveRooms() {
        log.debug("Getting all active rooms");
        
        List<Room> rooms = roomRepository.findByIsActiveTrue();
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get room by ID
     */
    public RoomDto.Response getRoomById(Long id) {
        log.debug("Getting room by ID: {}", id);
        
        Room room = findRoomById(id);
        return roomMapper.toResponse(room);
    }

    /**
     * Get room by room number
     */
    public RoomDto.Response getRoomByNumber(String roomNumber) {
        log.debug("Getting room by number: {}", roomNumber);
        
        Room room = roomRepository.findByRoomNumber(roomNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with number: " + roomNumber));
        
        return roomMapper.toResponse(room);
    }

    /**
     * Create new room
     */
    @Transactional
    public RoomDto.Response createRoom(RoomDto.CreateRequest request) {
        log.info("Creating new room: {}", request.getRoomNumber());
        
        // Validate unique room number
        validateUniqueRoomNumber(request.getRoomNumber(), null);
        
        // Validate room type exists and is active
        validateRoomTypeExists(request.getRoomTypeId());
        
        // Create entity
        Room room = roomMapper.toEntity(request);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setIsActive(true);
        
        // Save
        Room savedRoom = roomRepository.save(room);
        log.info("Created room with ID: {}", savedRoom.getId());
        
        return roomMapper.toResponse(savedRoom);
    }

    /**
     * Update existing room
     */
    @Transactional
    public RoomDto.Response updateRoom(Long id, RoomDto.UpdateRequest request) {
        log.info("Updating room ID: {}", id);
        
        Room existingRoom = findRoomById(id);
        
        // Validate unique room number if changed
        if (request.getRoomNumber() != null && !request.getRoomNumber().equals(existingRoom.getRoomNumber())) {
            validateUniqueRoomNumber(request.getRoomNumber(), id);
        }
        
        // Validate room type if changed
        if (request.getRoomTypeId() != null && !request.getRoomTypeId().equals(existingRoom.getRoomTypeId())) {
            validateRoomTypeExists(request.getRoomTypeId());
        }
        
        // Update entity
        roomMapper.updateEntity(existingRoom, request);
        
        // Save
        Room updatedRoom = roomRepository.save(existingRoom);
        log.info("Updated room ID: {}", id);
        
        return roomMapper.toResponse(updatedRoom);
    }

    /**
     * Update room status
     */
    @Transactional
    public RoomDto.Response updateRoomStatus(Long id, RoomDto.StatusUpdateRequest request) {
        log.info("Updating room status for ID: {} to {}", id, request.getStatus());
        
        Room room = findRoomById(id);
        
        // Validate status transition
        validateStatusTransition(room.getStatus(), request.getStatus());
        
        // Update status
        room.updateStatus(request.getStatus(), request.getNotes());
        
        // Save
        Room updatedRoom = roomRepository.save(room);
        log.info("Updated room status for ID: {} to {}", id, request.getStatus());
        
        return roomMapper.toResponse(updatedRoom);
    }

    /**
     * Delete room (soft delete)
     */
    @Transactional
    public void deleteRoom(Long id) {
        log.info("Deleting room ID: {}", id);
        
        Room room = findRoomById(id);
        
        // Check if room can be deleted (not occupied)
        if (room.isOccupied()) {
            throw new BusinessLogicException("Cannot delete occupied room. Please check out guests first.");
        }
        
        // Soft delete
        room.setIsActive(false);
        roomRepository.save(room);
        
        log.info("Deleted room ID: {}", id);
    }

    /**
     * Search rooms with filters
     */
    public Page<RoomDto.Response> searchRooms(RoomDto.FilterRequest filters, Pageable pageable) {
        log.debug("Searching rooms with filters: {}", filters);
        
        Page<Room> rooms = roomRepository.findRoomsWithFilters(
            filters.getStatus(),
            filters.getRoomTypeId(),
            filters.getFloor(),
            filters.getViewType(),
            filters.getHasBalcony(),
            filters.getMinPrice(),
            filters.getMaxPrice(),
            filters.getIsActive(),
            pageable
        );
        
        return rooms.map(roomMapper::toResponse);
    }

    /**
     * Get available rooms
     */
    public List<RoomDto.ListItem> getAvailableRooms() {
        log.debug("Getting available rooms");
        
        List<Room> rooms = roomRepository.findAvailableRooms();
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get available rooms by room type
     */
    public List<RoomDto.ListItem> getAvailableRoomsByType(Long roomTypeId) {
        log.debug("Getting available rooms by type: {}", roomTypeId);
        
        // Validate room type exists
        validateRoomTypeExists(roomTypeId);
        
        List<Room> rooms = roomRepository.findAvailableRoomsByType(roomTypeId);
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get rooms by status
     */
    public List<RoomDto.ListItem> getRoomsByStatus(RoomStatus status) {
        log.debug("Getting rooms by status: {}", status);
        
        List<Room> rooms = roomRepository.findByStatusAndIsActiveTrue(status);
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get rooms by floor
     */
    public List<RoomDto.ListItem> getRoomsByFloor(Integer floor) {
        log.debug("Getting rooms by floor: {}", floor);
        
        List<Room> rooms = roomRepository.findByFloorAndIsActiveTrue(floor);
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get rooms needing maintenance
     */
    public List<RoomDto.ListItem> getRoomsNeedingMaintenance() {
        log.debug("Getting rooms needing maintenance");
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        List<Room> rooms = roomRepository.findRoomsNeedingMaintenance(cutoffDate);
        return roomMapper.toListItems(rooms);
    }

    /**
     * Get room statistics
     */
    public RoomDto.Statistics getRoomStatistics() {
        log.debug("Getting room statistics");
        
        long totalRooms = roomRepository.countByIsActiveTrue();
        long availableRooms = roomRepository.countByStatusAndIsActiveTrue(RoomStatus.AVAILABLE);
        long occupiedRooms = roomRepository.countByStatusAndIsActiveTrue(RoomStatus.OCCUPIED);
        long maintenanceRooms = roomRepository.countByStatusAndIsActiveTrue(RoomStatus.MAINTENANCE);
        long outOfOrderRooms = roomRepository.countByStatusAndIsActiveTrue(RoomStatus.OUT_OF_ORDER);
        
        Double occupancyRate = roomRepository.getOccupancyRate();
        if (occupancyRate == null) occupancyRate = 0.0;
        
        // Get room type distribution
        List<Object[]> typeStats = roomRepository.getRoomStatisticsByType();
        List<RoomDto.Statistics.RoomTypeDistribution> distribution = typeStats.stream()
            .map(stat -> RoomDto.Statistics.RoomTypeDistribution.builder()
                .typeName((String) stat[0])
                .count((Long) stat[1])
                .percentage(totalRooms > 0 ? ((Long) stat[1] * 100.0) / totalRooms : 0.0)
                .build())
            .toList();
        
        return RoomDto.Statistics.builder()
            .totalRooms(totalRooms)
            .availableRooms(availableRooms)
            .occupiedRooms(occupiedRooms)
            .maintenanceRooms(maintenanceRooms)
            .outOfOrderRooms(outOfOrderRooms)
            .occupancyRate(occupancyRate)
            .roomTypeDistribution(distribution)
            .build();
    }

    /**
     * Activate/Deactivate room
     */
    @Transactional
    public RoomDto.Response toggleRoomStatus(Long id) {
        log.info("Toggling status for room ID: {}", id);
        
        Room room = findRoomById(id);
        
        // If deactivating, check if room is occupied
        if (room.getIsActive() && room.isOccupied()) {
            throw new BusinessLogicException("Cannot deactivate occupied room. Please check out guests first.");
        }
        
        room.setIsActive(!room.getIsActive());
        Room updatedRoom = roomRepository.save(room);
        
        log.info("Toggled status for room ID: {} to {}", id, updatedRoom.getIsActive());
        return roomMapper.toResponse(updatedRoom);
    }

    // Helper methods

    private Room findRoomById(Long id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
    }

    private void validateUniqueRoomNumber(String roomNumber, Long excludeId) {
        boolean exists = excludeId == null 
            ? roomRepository.findByRoomNumber(roomNumber).isPresent()
            : roomRepository.existsByRoomNumberAndIdNot(roomNumber, excludeId);
            
        if (exists) {
            throw new DuplicateResourceException("Room with number '" + roomNumber + "' already exists");
        }
    }

    private void validateRoomTypeExists(Long roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + roomTypeId));
            
        if (!roomType.getIsActive()) {
            throw new BusinessLogicException("Cannot assign room to inactive room type");
        }
    }

    private void validateStatusTransition(RoomStatus currentStatus, RoomStatus newStatus) {
        // Define valid status transitions
        boolean isValidTransition = switch (currentStatus) {
            case AVAILABLE -> newStatus == RoomStatus.OCCUPIED || 
                           newStatus == RoomStatus.MAINTENANCE || 
                           newStatus == RoomStatus.OUT_OF_ORDER;
            case OCCUPIED -> newStatus == RoomStatus.AVAILABLE || 
                           newStatus == RoomStatus.MAINTENANCE;
            case MAINTENANCE -> newStatus == RoomStatus.AVAILABLE || 
                              newStatus == RoomStatus.OUT_OF_ORDER;
            case OUT_OF_ORDER -> newStatus == RoomStatus.MAINTENANCE || 
                               newStatus == RoomStatus.AVAILABLE;
        };
        
        if (!isValidTransition) {
            throw new BusinessLogicException(
                String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }
    }
}