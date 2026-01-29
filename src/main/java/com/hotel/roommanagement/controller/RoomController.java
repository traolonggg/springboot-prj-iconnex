package com.hotel.roommanagement.controller;

import com.hotel.roommanagement.dto.RoomDto;
import com.hotel.roommanagement.enums.RoomStatus;
import com.hotel.roommanagement.service.RoomService;
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

import java.util.List;

/**
 * REST Controller for Room operations
 * 
 * Provides endpoints for managing rooms including
 * CRUD operations, search, filtering, and status management.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Room management operations")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Get all rooms", description = "Retrieve all rooms with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping
    public ResponseEntity<Page<RoomDto.Response>> getAllRooms(
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<RoomDto.Response> rooms = roomService.getAllRooms(pageable);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get active rooms", description = "Retrieve all active rooms")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active rooms")
    @GetMapping("/active")
    public ResponseEntity<List<RoomDto.ListItem>> getActiveRooms() {
        List<RoomDto.ListItem> rooms = roomService.getActiveRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get room by ID", description = "Retrieve a specific room by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved room"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto.Response> getRoomById(
            @Parameter(description = "Room ID") @PathVariable Long id) {
        
        RoomDto.Response room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @Operation(summary = "Get room by number", description = "Retrieve a specific room by its room number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved room"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/number/{roomNumber}")
    public ResponseEntity<RoomDto.Response> getRoomByNumber(
            @Parameter(description = "Room number") @PathVariable String roomNumber) {
        
        RoomDto.Response room = roomService.getRoomByNumber(roomNumber);
        return ResponseEntity.ok(room);
    }

    @Operation(summary = "Create room", description = "Create a new room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Room created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Room with same number already exists")
    })
    @PostMapping
    public ResponseEntity<RoomDto.Response> createRoom(
            @Valid @RequestBody RoomDto.CreateRequest request) {
        
        RoomDto.Response createdRoom = roomService.createRoom(request);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    @Operation(summary = "Update room", description = "Update an existing room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "409", description = "Room with same number already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto.Response> updateRoom(
            @Parameter(description = "Room ID") @PathVariable Long id,
            @Valid @RequestBody RoomDto.UpdateRequest request) {
        
        RoomDto.Response updatedRoom = roomService.updateRoom(id, request);
        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(summary = "Update room status", description = "Update the status of a room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status transition"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<RoomDto.Response> updateRoomStatus(
            @Parameter(description = "Room ID") @PathVariable Long id,
            @Valid @RequestBody RoomDto.StatusUpdateRequest request) {
        
        RoomDto.Response updatedRoom = roomService.updateRoomStatus(id, request);
        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(summary = "Delete room", description = "Delete a room (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Room deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "400", description = "Cannot delete occupied room")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @Parameter(description = "Room ID") @PathVariable Long id) {
        
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search rooms", description = "Search rooms with filters")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    @PostMapping("/search")
    public ResponseEntity<Page<RoomDto.Response>> searchRooms(
            @RequestBody RoomDto.FilterRequest filters,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<RoomDto.Response> rooms = roomService.searchRooms(filters, pageable);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get available rooms", description = "Get all available rooms")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available rooms")
    @GetMapping("/available")
    public ResponseEntity<List<RoomDto.ListItem>> getAvailableRooms() {
        List<RoomDto.ListItem> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get available rooms by type", description = "Get available rooms of a specific type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available rooms"),
        @ApiResponse(responseCode = "404", description = "Room type not found")
    })
    @GetMapping("/available/type/{roomTypeId}")
    public ResponseEntity<List<RoomDto.ListItem>> getAvailableRoomsByType(
            @Parameter(description = "Room type ID") @PathVariable Long roomTypeId) {
        
        List<RoomDto.ListItem> rooms = roomService.getAvailableRoomsByType(roomTypeId);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get rooms by status", description = "Get rooms filtered by status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RoomDto.ListItem>> getRoomsByStatus(
            @Parameter(description = "Room status") @PathVariable RoomStatus status) {
        
        List<RoomDto.ListItem> rooms = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get rooms by floor", description = "Get rooms on a specific floor")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms by floor")
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<RoomDto.ListItem>> getRoomsByFloor(
            @Parameter(description = "Floor number") @PathVariable Integer floor) {
        
        List<RoomDto.ListItem> rooms = roomService.getRoomsByFloor(floor);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get rooms needing maintenance", description = "Get rooms that need maintenance")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved rooms needing maintenance")
    @GetMapping("/maintenance-needed")
    public ResponseEntity<List<RoomDto.ListItem>> getRoomsNeedingMaintenance() {
        List<RoomDto.ListItem> rooms = roomService.getRoomsNeedingMaintenance();
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "Get room statistics", description = "Get comprehensive room statistics")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved room statistics")
    @GetMapping("/statistics")
    public ResponseEntity<RoomDto.Statistics> getRoomStatistics() {
        RoomDto.Statistics statistics = roomService.getRoomStatistics();
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "Toggle room status", description = "Activate or deactivate a room")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Room not found"),
        @ApiResponse(responseCode = "400", description = "Cannot deactivate occupied room")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<RoomDto.Response> toggleRoomStatus(
            @Parameter(description = "Room ID") @PathVariable Long id) {
        
        RoomDto.Response room = roomService.toggleRoomStatus(id);
        return ResponseEntity.ok(room);
    }
}