package com.hotel.roommanagement.entity;

import com.hotel.roommanagement.enums.RoomStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room Entity
 * 
 * Represents individual room instances in the hotel.
 * Each room belongs to a specific room type and has its own
 * characteristics, status, and maintenance history.
 */
@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true, length = 10)
    @NotBlank(message = "Room number is required")
    @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
    private String roomNumber;

    @Column(name = "room_type_id", nullable = false)
    @NotNull(message = "Room type is required")
    private Long roomTypeId;

    @Column(name = "floor", nullable = false)
    @NotNull(message = "Floor is required")
    @Min(value = 1, message = "Floor must be at least 1")
    @Max(value = 50, message = "Floor cannot exceed 50")
    private Integer floor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Column(name = "view_type", length = 50)
    @Size(max = 50, message = "View type cannot exceed 50 characters")
    private String viewType;

    @Column(name = "has_balcony", nullable = false)
    @Builder.Default
    private Boolean hasBalcony = false;

    @Column(name = "wifi_password", length = 50)
    @Size(max = 50, message = "WiFi password cannot exceed 50 characters")
    private String wifiPassword;

    @Column(name = "last_maintenance")
    private LocalDateTime lastMaintenance;

    @Column(name = "notes", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private RoomType roomType;

    /**
     * Check if room is available for booking
     */
    public boolean isAvailable() {
        return RoomStatus.AVAILABLE.equals(status) && Boolean.TRUE.equals(isActive);
    }

    /**
     * Check if room is currently occupied
     */
    public boolean isOccupied() {
        return RoomStatus.OCCUPIED.equals(status) && Boolean.TRUE.equals(isActive);
    }

    /**
     * Check if room is under maintenance
     */
    public boolean isUnderMaintenance() {
        return RoomStatus.MAINTENANCE.equals(status);
    }

    /**
     * Check if room is out of order
     */
    public boolean isOutOfOrder() {
        return RoomStatus.OUT_OF_ORDER.equals(status);
    }

    /**
     * Get current room price from room type
     */
    public BigDecimal getCurrentPrice() {
        return roomType != null ? roomType.getBasePrice() : BigDecimal.ZERO;
    }

    /**
     * Update room status and set maintenance date if needed
     */
    public void updateStatus(RoomStatus newStatus, String statusNotes) {
        this.status = newStatus;
        if (statusNotes != null && !statusNotes.trim().isEmpty()) {
            this.notes = statusNotes;
        }
        
        // Update maintenance date if status is maintenance
        if (RoomStatus.MAINTENANCE.equals(newStatus)) {
            this.lastMaintenance = LocalDateTime.now();
        }
    }

    /**
     * Get room display name (room number + floor)
     */
    public String getDisplayName() {
        return String.format("Room %s (Floor %d)", roomNumber, floor);
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = RoomStatus.AVAILABLE;
        }
        if (hasBalcony == null) {
            hasBalcony = false;
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}