package com.hotel.roommanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Room Type Entity
 * 
 * Represents different categories of rooms in the hotel.
 * Each room type defines the characteristics, pricing, and amenities
 * that rooms of this type will have.
 */
@Entity
@Table(name = "room_types")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Room type name is required")
    @Size(min = 1, max = 100, message = "Room type name must be between 1 and 100 characters")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Base price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal basePrice;

    @Column(name = "max_occupancy", nullable = false)
    @NotNull(message = "Max occupancy is required")
    @Min(value = 1, message = "Max occupancy must be at least 1")
    @Max(value = 10, message = "Max occupancy cannot exceed 10")
    private Integer maxOccupancy;

    @Column(name = "size_sqm")
    @Min(value = 1, message = "Room size must be at least 1 square meter")
    private Integer sizeSqm;

    @Column(name = "amenities", columnDefinition = "TEXT")
    private String amenities; // JSON string of amenities list

    @Column(name = "image_url")
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Room> rooms;

    /**
     * Get the number of rooms of this type
     */
    public int getRoomCount() {
        return rooms != null ? rooms.size() : 0;
    }

    /**
     * Get the number of active rooms of this type
     */
    public long getActiveRoomCount() {
        return rooms != null ? 
            rooms.stream().filter(Room::getIsActive).count() : 0;
    }

    /**
     * Check if this room type can be deleted
     * (no active rooms using this type)
     */
    public boolean canBeDeleted() {
        return getActiveRoomCount() == 0;
    }

    @PrePersist
    protected void onCreate() {
        if (isActive == null) {
            isActive = true;
        }
    }
}