package com.hotel.roommanagement.enums;

import lombok.Getter;

/**
 * Room Status Enumeration
 * 
 * Defines the possible statuses that a room can have in the hotel management system.
 * Each status represents a different operational state of the room.
 */
@Getter
public enum RoomStatus {
    
    /**
     * Room is available for booking and occupancy
     */
    AVAILABLE("Available", "Room is ready for booking and occupancy"),
    
    /**
     * Room is currently occupied by guests
     */
    OCCUPIED("Occupied", "Room is currently occupied by guests"),
    
    /**
     * Room is under maintenance and not available for booking
     */
    MAINTENANCE("Maintenance", "Room is under maintenance and temporarily unavailable"),
    
    /**
     * Room is out of order due to serious issues
     */
    OUT_OF_ORDER("Out of Order", "Room is out of order and cannot be used");

    private final String displayName;
    private final String description;

    RoomStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get room status from string value (case-insensitive)
     */
    public static RoomStatus fromString(String status) {
        if (status == null || status.trim().isEmpty()) {
            return AVAILABLE; // Default status
        }
        
        try {
            return RoomStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid room status: " + status + 
                ". Valid values are: AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_ORDER");
        }
    }

    /**
     * Check if the status allows room booking
     */
    public boolean isBookable() {
        return this == AVAILABLE;
    }

    /**
     * Check if the status indicates room is in use
     */
    public boolean isInUse() {
        return this == OCCUPIED;
    }

    /**
     * Check if the status indicates room is unavailable
     */
    public boolean isUnavailable() {
        return this == MAINTENANCE || this == OUT_OF_ORDER;
    }

    @Override
    public String toString() {
        return displayName;
    }
}