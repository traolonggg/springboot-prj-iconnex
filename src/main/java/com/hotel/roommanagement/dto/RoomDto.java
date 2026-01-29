package com.hotel.roommanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel.roommanagement.enums.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room Data Transfer Objects
 */
public class RoomDto {

    /**
     * DTO for creating a new room
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room creation request")
    public static class CreateRequest {
        
        @Schema(description = "Room number", example = "201", required = true)
        @NotBlank(message = "Room number is required")
        @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
        private String roomNumber;

        @Schema(description = "Room type ID", example = "1", required = true)
        @NotNull(message = "Room type is required")
        private Long roomTypeId;

        @Schema(description = "Floor number", example = "2", required = true)
        @NotNull(message = "Floor is required")
        @Min(value = 1, message = "Floor must be at least 1")
        @Max(value = 50, message = "Floor cannot exceed 50")
        private Integer floor;

        @Schema(description = "View type", example = "Sea view")
        @Size(max = 50, message = "View type cannot exceed 50 characters")
        private String viewType;

        @Schema(description = "Has balcony", example = "true")
        private Boolean hasBalcony;

        @Schema(description = "WiFi password", example = "hotel201")
        @Size(max = 50, message = "WiFi password cannot exceed 50 characters")
        private String wifiPassword;

        @Schema(description = "Special notes", example = "Corner room with panoramic view")
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
    }

    /**
     * DTO for updating an existing room
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room update request")
    public static class UpdateRequest {
        
        @Schema(description = "Room number", example = "201")
        @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
        private String roomNumber;

        @Schema(description = "Room type ID", example = "1")
        private Long roomTypeId;

        @Schema(description = "Floor number", example = "2")
        @Min(value = 1, message = "Floor must be at least 1")
        @Max(value = 50, message = "Floor cannot exceed 50")
        private Integer floor;

        @Schema(description = "Room status", example = "AVAILABLE")
        private RoomStatus status;

        @Schema(description = "View type", example = "Sea view")
        @Size(max = 50, message = "View type cannot exceed 50 characters")
        private String viewType;

        @Schema(description = "Has balcony", example = "true")
        private Boolean hasBalcony;

        @Schema(description = "WiFi password", example = "hotel201")
        @Size(max = 50, message = "WiFi password cannot exceed 50 characters")
        private String wifiPassword;

        @Schema(description = "Special notes", example = "Corner room with panoramic view")
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;

        @Schema(description = "Whether room is active", example = "true")
        private Boolean isActive;
    }

    /**
     * DTO for updating room status
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room status update request")
    public static class StatusUpdateRequest {
        
        @Schema(description = "New room status", example = "MAINTENANCE", required = true)
        @NotNull(message = "Room status is required")
        private RoomStatus status;

        @Schema(description = "Status change notes", example = "Scheduled maintenance for air conditioning")
        @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        private String notes;
    }

    /**
     * DTO for room response
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room response")
    public static class Response {
        
        @Schema(description = "Room ID", example = "1")
        private Long id;
        
        @Schema(description = "Room number", example = "201", required = true)
        private String roomNumber;

        @Schema(description = "Room type ID", example = "1", required = true)
        private Long roomTypeId;

        @Schema(description = "Floor number", example = "2", required = true)
        private Integer floor;

        @Schema(description = "Room status", example = "AVAILABLE")
        private RoomStatus status;

        @Schema(description = "View type", example = "Sea view")
        private String viewType;

        @Schema(description = "Has balcony", example = "true")
        private Boolean hasBalcony;

        @Schema(description = "WiFi password", example = "hotel201")
        private String wifiPassword;

        @Schema(description = "Special notes", example = "Corner room with panoramic view")
        private String notes;

        @Schema(description = "Last maintenance date", example = "2023-11-15T14:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime lastMaintenance;

        @Schema(description = "Whether room is active", example = "true")
        private Boolean isActive;

        @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;

        @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;

        @Schema(description = "Room type information")
        private RoomTypeDto.Response roomType;

        @Schema(description = "Current room price", example = "150.00")
        private BigDecimal currentPrice;

        @Schema(description = "Room display name", example = "Room 201 (Floor 2)")
        private String displayName;
    }

    /**
     * Simplified DTO for room list responses
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room list item")
    public static class ListItem {
        
        @Schema(description = "Room ID", example = "1")
        private Long id;

        @Schema(description = "Room number", example = "201")
        private String roomNumber;

        @Schema(description = "Floor number", example = "2")
        private Integer floor;

        @Schema(description = "Room status", example = "AVAILABLE")
        private RoomStatus status;

        @Schema(description = "View type", example = "Sea view")
        private String viewType;

        @Schema(description = "Has balcony", example = "true")
        private Boolean hasBalcony;

        @Schema(description = "Whether room is active", example = "true")
        private Boolean isActive;

        @Schema(description = "Room type name", example = "Deluxe Room")
        private String roomTypeName;

        @Schema(description = "Current room price", example = "150.00")
        private BigDecimal currentPrice;

        @Schema(description = "Room display name", example = "Room 201 (Floor 2)")
        private String displayName;
    }

    /**
     * DTO for room filter parameters
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room filter parameters")
    public static class FilterRequest {
        
        @Schema(description = "Filter by room status", example = "AVAILABLE")
        private RoomStatus status;

        @Schema(description = "Filter by room type ID", example = "1")
        private Long roomTypeId;

        @Schema(description = "Filter by floor number", example = "2")
        private Integer floor;

        @Schema(description = "Filter by view type", example = "Sea view")
        private String viewType;

        @Schema(description = "Filter by balcony availability", example = "true")
        private Boolean hasBalcony;

        @Schema(description = "Filter by active status", example = "true")
        private Boolean isActive;

        @Schema(description = "Minimum price filter", example = "100.00")
        @DecimalMin(value = "0.00", message = "Minimum price cannot be negative")
        private BigDecimal minPrice;

        @Schema(description = "Maximum price filter", example = "300.00")
        @DecimalMin(value = "0.00", message = "Maximum price cannot be negative")
        private BigDecimal maxPrice;
    }

    /**
     * DTO for room statistics
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room statistics")
    public static class Statistics {
        
        @Schema(description = "Total number of rooms", example = "100")
        private Long totalRooms;

        @Schema(description = "Number of available rooms", example = "75")
        private Long availableRooms;

        @Schema(description = "Number of occupied rooms", example = "20")
        private Long occupiedRooms;

        @Schema(description = "Number of rooms under maintenance", example = "3")
        private Long maintenanceRooms;

        @Schema(description = "Number of rooms out of order", example = "2")
        private Long outOfOrderRooms;

        @Schema(description = "Occupancy rate percentage", example = "20.0")
        private Double occupancyRate;

        @Schema(description = "Room type distribution")
        private java.util.List<RoomTypeDistribution> roomTypeDistribution;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "Room type distribution item")
        public static class RoomTypeDistribution {
            
            @Schema(description = "Room type name", example = "Deluxe Room")
            private String typeName;

            @Schema(description = "Number of rooms of this type", example = "25")
            private Long count;

            @Schema(description = "Percentage of total rooms", example = "25.0")
            private Double percentage;
        }
    }
}