package com.hotel.roommanagement.controller;

import com.hotel.roommanagement.dto.RoomTypeDto;
import com.hotel.roommanagement.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Room Type operations
 * 
 * Provides endpoints for managing room types including
 * CRUD operations, search, and filtering capabilities.
 */
@RestController
@RequestMapping("/api/v1/room-types")
@RequiredArgsConstructor
@Tag(name = "Room Types", description = "Room type management operations")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Operation(summary = "Get all room types", description = "Retrieve all room types with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved room types"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public ResponseEntity<Page<RoomTypeDto.Response>> getAllRoomTypes(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<RoomTypeDto.Response> roomTypes = roomTypeService.getAllRoomTypes(pageable);
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Get active room types", description = "Retrieve all active room types")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active room types")
    @GetMapping("/active")
    public ResponseEntity<List<RoomTypeDto.ListItem>> getActiveRoomTypes() {
        List<RoomTypeDto.ListItem> roomTypes = roomTypeService.getActiveRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Get room type by ID", description = "Retrieve a specific room type by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved room type"),
        @ApiResponse(responseCode = "404", description = "Room type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDto.Response> getRoomTypeById(
            @Parameter(description = "Room type ID") @PathVariable Long id) {
        
        RoomTypeDto.Response roomType = roomTypeService.getRoomTypeById(id);
        return ResponseEntity.ok(roomType);
    }

    @Operation(summary = "Create room type", description = "Create a new room type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room type created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Room type with same name already exists")
    })
    @PostMapping
    public ResponseEntity<RoomTypeDto.Response> createRoomType(
            @Valid @RequestBody RoomTypeDto.CreateRequest request) {
        
        RoomTypeDto.Response createdRoomType = roomTypeService.createRoomType(request);
        return new ResponseEntity<>(createdRoomType, HttpStatus.CREATED);
    }

    @Operation(summary = "Update room type", description = "Update an existing room type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room type updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Room type not found"),
        @ApiResponse(responseCode = "409", description = "Room type with same name already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDto.Response> updateRoomType(
            @Parameter(description = "Room type ID") @PathVariable Long id,
            @Valid @RequestBody RoomTypeDto.UpdateRequest request) {
        
        RoomTypeDto.Response updatedRoomType = roomTypeService.updateRoomType(id, request);
        return ResponseEntity.ok(updatedRoomType);
    }

    @Operation(summary = "Delete room type", description = "Delete a room type (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Room type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Room type not found"),
        @ApiResponse(responseCode = "400", description = "Cannot delete room type with active rooms")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(
            @Parameter(description = "Room type ID") @PathVariable Long id) {
        
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search room types", description = "Search room types by name or description")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    @GetMapping("/search")
    public ResponseEntity<Page<RoomTypeDto.Response>> searchRoomTypes(
            @Parameter(description = "Search term") @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<RoomTypeDto.Response> roomTypes = roomTypeService.searchRoomTypes(q, pageable);
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Get room types by price range", description = "Filter room types by price range")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered room types")
    @GetMapping("/price-range")
    public ResponseEntity<List<RoomTypeDto.ListItem>> getRoomTypesByPriceRange(
            @Parameter(description = "Minimum price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam BigDecimal maxPrice) {
        
        List<RoomTypeDto.ListItem> roomTypes = roomTypeService.getRoomTypesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Get room types by occupancy", description = "Filter room types by minimum occupancy")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered room types")
    @GetMapping("/occupancy")
    public ResponseEntity<List<RoomTypeDto.ListItem>> getRoomTypesByMinOccupancy(
            @Parameter(description = "Minimum occupancy") @RequestParam Integer minOccupancy) {
        
        List<RoomTypeDto.ListItem> roomTypes = roomTypeService.getRoomTypesByMinOccupancy(minOccupancy);
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Get room types with available rooms", description = "Get room types that have available rooms")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved room types with available rooms")
    @GetMapping("/available")
    public ResponseEntity<List<RoomTypeDto.ListItem>> getRoomTypesWithAvailableRooms() {
        List<RoomTypeDto.ListItem> roomTypes = roomTypeService.getRoomTypesWithAvailableRooms();
        return ResponseEntity.ok(roomTypes);
    }

    @Operation(summary = "Toggle room type status", description = "Activate or deactivate a room type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room type status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Room type not found"),
        @ApiResponse(responseCode = "400", description = "Cannot deactivate room type with active rooms")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<RoomTypeDto.Response> toggleRoomTypeStatus(
            @Parameter(description = "Room type ID") @PathVariable Long id) {
        
        RoomTypeDto.Response roomType = roomTypeService.toggleRoomTypeStatus(id);
        return ResponseEntity.ok(roomType);
    }
}