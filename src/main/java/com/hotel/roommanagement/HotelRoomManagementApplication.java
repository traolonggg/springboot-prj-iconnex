package com.hotel.roommanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Hotel Room Management System Application
 * 
 * Main application class for the Hotel Room Management System built with Spring Boot.
 * This system provides comprehensive room management functionality including:
 * - Room Type Management (CRUD operations)
 * - Room Management (CRUD operations with advanced filtering)
 * - Room Status Management
 * - Room Statistics and Reporting
 * 
 * @author Hotel Management Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class HotelRoomManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelRoomManagementApplication.class, args);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏨 Hotel Room Management System Started Successfully!");
        System.out.println("📖 API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("🔍 H2 Console: http://localhost:8080/h2-console");
        System.out.println("🚀 API Base URL: http://localhost:8080/api/v1");
        System.out.println("=".repeat(60));
    }
}