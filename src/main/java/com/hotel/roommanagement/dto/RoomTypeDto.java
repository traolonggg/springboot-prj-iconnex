package com.hotel.roommanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room Type Data Transfer Objects
 */
public class RoomTypeDto {

    /**
     * DTO for creating a new room type
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room type creation request")
    public static class CreateRequest {
        
        @Schema(description = "Room type name", example = "Deluxe Room", required = true)
        @NotBlank(message = "Room type name is required")
        @Size(min = 1, max = 100, message = "Room type name must be between 1 and 100 characters")
        private String name;

        @Schema(description = "Room type description", example = "Spacious room with premium amenities")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @Schema(description = "Base price per night", example = "150.00", required = true)
        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Base price must have at most 8 integer digits and 2 decimal places")
        private BigDecimal basePrice;

        @Schema(description = "Maximum number of guests", example = "3", required = true)
        @NotNull(message = "Max occupancy is required")
        @Min(value = 1, message = "Max occupancy must be at least 1")
        @Max(value = 10, message = "Max occupancy cannot exceed 10")
        private Integer maxOccupancy;

        @Schema(description = "Room size in square meters", example = "35")
        @Min(value = 1, message = "Room size must be at least 1 square meter")
        private Integer sizeSqm;

        @Schema(description = "JSON string of amenities list", 
                example = "[\"Air Conditioning\", \"TV\", \"WiFi\", \"Mini Bar\"]")
        private String amenities;

        @Schema(description = "Room type image URL", 
                example = "https://example.com/images/deluxe-room.jpg")
        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        private String imageUrl;
    }

    /**
     * DTO for updating an existing room type
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room type update request")
    public static class UpdateRequest {
        
        @Schema(description = "Room type name", example = "Deluxe Room")
        @Size(min = 1, max = 100, message = "Room type name must be between 1 and 100 characters")
        private String name;

        @Schema(description = "Room type description", example = "Spacious room with premium amenities")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @Schema(description = "Base price per night", example = "150.00")
        @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Base price must have at most 8 integer digits and 2 decimal places")
        private BigDecimal basePrice;

        @Schema(description = "Maximum number of guests", example = "3")
        @Min(value = 1, message = "Max occupancy must be at least 1")
        @Max(value = 10, message = "Max occupancy cannot exceed 10")
        private Integer maxOccupancy;

        @Schema(description = "Room size in square meters", example = "35")
        @Min(value = 1, message = "Room size must be at least 1 square meter")
        private Integer sizeSqm;

        @Schema(description = "JSON string of amenities list")
        private String amenities;

        @Schema(description = "Room type image URL")
        @Size(max = 255, message = "Image URL cannot exceed 255 characters")
        private String imageUrl;

        @Schema(description = "Whether room type is active", example = "true")
        private Boolean isActive;
    }

    /**
     * DTO for room type response
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room type response")
    public static class Response {
        
        @Schema(description = "Room type ID", example = "1")
        private Long id;
        
        @Schema(description = "Room type name", example = "Deluxe Room", required = true)
        private String name;

        @Schema(description = "Room type description", example = "Spacious room with premium amenities")
        private String description;

        @Schema(description = "Base price per night", example = "150.00", required = true)
        private BigDecimal basePrice;

        @Schema(description = "Maximum number of guests", example = "3", required = true)
        private Integer maxOccupancy;

        @Schema(description = "Room size in square meters", example = "35")
        private Integer sizeSqm;

        @Schema(description = "JSON string of amenities list", 
                example = "[\"Air Conditioning\", \"TV\", \"WiFi\", \"Mini Bar\"]")
        private String amenities;

        @Schema(description = "Room type image URL", 
                example = "https://example.com/images/deluxe-room.jpg")
        private String imageUrl;

        @Schema(description = "Whether room type is active", example = "true")
        private Boolean isActive;

        @Schema(description = "Number of rooms of this type", example = "10")
        private Integer roomCount;

        @Schema(description = "Number of active rooms of this type", example = "8")
        private Long activeRoomCount;

        @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;

        @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    /**
     * Simplified DTO for room type list responses
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Room type list item")
    public static class ListItem {
        
        @Schema(description = "Room type ID", example = "1")
        private Long id;

        @Schema(description = "Room type name", example = "Deluxe Room")
        private String name;

        @Schema(description = "Base price per night", example = "150.00")
        private BigDecimal basePrice;

        @Schema(description = "Maximum number of guests", example = "3")
        private Integer maxOccupancy;

        @Schema(description = "Room size in square meters", example = "35")
        private Integer sizeSqm;

        @Schema(description = "Whether room type is active", example = "true")
        private Boolean isActive;

        @Schema(description = "Number of rooms of this type", example = "10")
        private Integer roomCount;
    }
}