package com.hotel.roommanagement.service;

import com.hotel.roommanagement.dto.RoomTypeDto;
import com.hotel.roommanagement.entity.RoomType;
import com.hotel.roommanagement.exception.ResourceNotFoundException;
import com.hotel.roommanagement.exception.DuplicateResourceException;
import com.hotel.roommanagement.exception.BusinessLogicException;
import com.hotel.roommanagement.mapper.RoomTypeMapper;
import com.hotel.roommanagement.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for Room Type operations
 * 
 * Handles business logic for room type management including
 * CRUD operations, validation, and business rules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    /**
     * Get all room types with pagination
     */
    public Page<RoomTypeDto.Response> getAllRoomTypes(Pageable pageable) {
        log.debug("Getting all room types with pagination: {}", pageable);
        
        Page<RoomType> roomTypes = roomTypeRepository.findAll(pageable);
        return roomTypes.map(roomTypeMapper::toResponse);
    }

    /**
     * Get all active room types
     */
    public List<RoomTypeDto.ListItem> getActiveRoomTypes() {
        log.debug("Getting all active room types");
        
        List<RoomType> roomTypes = roomTypeRepository.findByIsActiveTrue();
        return roomTypeMapper.toListItems(roomTypes);
    }

    /**
     * Get room type by ID
     */
    public RoomTypeDto.Response getRoomTypeById(Long id) {
        log.debug("Getting room type by ID: {}", id);
        
        RoomType roomType = findRoomTypeById(id);
        return roomTypeMapper.toResponse(roomType);
    }

    /**
     * Create new room type
     */
    @Transactional
    public RoomTypeDto.Response createRoomType(RoomTypeDto.CreateRequest request) {
        log.info("Creating new room type: {}", request.getName());
        
        // Validate unique name
        validateUniqueRoomTypeName(request.getName(), null);
        
        // Create entity
        RoomType roomType = roomTypeMapper.toEntity(request);
        roomType.setIsActive(true);
        
        // Save
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        log.info("Created room type with ID: {}", savedRoomType.getId());
        
        return roomTypeMapper.toResponse(savedRoomType);
    }

    /**
     * Update existing room type
     */
    @Transactional
    public RoomTypeDto.Response updateRoomType(Long id, RoomTypeDto.UpdateRequest request) {
        log.info("Updating room type ID: {}", id);
        
        RoomType existingRoomType = findRoomTypeById(id);
        
        // Validate unique name if changed
        if (request.getName() != null && !request.getName().equals(existingRoomType.getName())) {
            validateUniqueRoomTypeName(request.getName(), id);
        }
        
        // Update entity
        roomTypeMapper.updateEntity(existingRoomType, request);
        
        // Save
        RoomType updatedRoomType = roomTypeRepository.save(existingRoomType);
        log.info("Updated room type ID: {}", id);
        
        return roomTypeMapper.toResponse(updatedRoomType);
    }

    /**
     * Delete room type (soft delete)
     */
    @Transactional
    public void deleteRoomType(Long id) {
        log.info("Deleting room type ID: {}", id);
        
        RoomType roomType = findRoomTypeById(id);
        
        // Check if room type can be deleted
        if (!roomType.canBeDeleted()) {
            throw new BusinessLogicException(
                "Cannot delete room type with active rooms. Please deactivate or reassign all rooms first."
            );
        }
        
        // Soft delete
        roomType.setIsActive(false);
        roomTypeRepository.save(roomType);
        
        log.info("Deleted room type ID: {}", id);
    }

    /**
     * Search room types
     */
    public Page<RoomTypeDto.Response> searchRoomTypes(String searchTerm, Pageable pageable) {
        log.debug("Searching room types with term: {}", searchTerm);
        
        Page<RoomType> roomTypes = roomTypeRepository.searchRoomTypes(searchTerm, pageable);
        return roomTypes.map(roomTypeMapper::toResponse);
    }

    /**
     * Get room types by price range
     */
    public List<RoomTypeDto.ListItem> getRoomTypesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.debug("Getting room types by price range: {} - {}", minPrice, maxPrice);
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new BusinessLogicException("Minimum price cannot be greater than maximum price");
        }
        
        List<RoomType> roomTypes = roomTypeRepository.findByPriceRange(minPrice, maxPrice);
        return roomTypeMapper.toListItems(roomTypes);
    }

    /**
     * Get room types by minimum occupancy
     */
    public List<RoomTypeDto.ListItem> getRoomTypesByMinOccupancy(Integer minOccupancy) {
        log.debug("Getting room types by minimum occupancy: {}", minOccupancy);
        
        List<RoomType> roomTypes = roomTypeRepository.findByMaxOccupancyGreaterThanEqualAndIsActiveTrue(minOccupancy);
        return roomTypeMapper.toListItems(roomTypes);
    }

    /**
     * Get room types with available rooms
     */
    public List<RoomTypeDto.ListItem> getRoomTypesWithAvailableRooms() {
        log.debug("Getting room types with available rooms");
        
        List<RoomType> roomTypes = roomTypeRepository.findRoomTypesWithAvailableRooms();
        return roomTypeMapper.toListItems(roomTypes);
    }

    /**
     * Activate/Deactivate room type
     */
    @Transactional
    public RoomTypeDto.Response toggleRoomTypeStatus(Long id) {
        log.info("Toggling status for room type ID: {}", id);
        
        RoomType roomType = findRoomTypeById(id);
        
        // If deactivating, check if it has active rooms
        if (roomType.getIsActive() && roomType.getActiveRoomCount() > 0) {
            throw new BusinessLogicException(
                "Cannot deactivate room type with active rooms. Please deactivate all rooms first."
            );
        }
        
        roomType.setIsActive(!roomType.getIsActive());
        RoomType updatedRoomType = roomTypeRepository.save(roomType);
        
        log.info("Toggled status for room type ID: {} to {}", id, updatedRoomType.getIsActive());
        return roomTypeMapper.toResponse(updatedRoomType);
    }

    // Helper methods

    private RoomType findRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));
    }

    private void validateUniqueRoomTypeName(String name, Long excludeId) {
        boolean exists = excludeId == null 
            ? roomTypeRepository.findByNameIgnoreCase(name).isPresent()
            : roomTypeRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId);
            
        if (exists) {
            throw new DuplicateResourceException("Room type with name '" + name + "' already exists");
        }
    }
}