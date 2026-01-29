# Hotel Room Management System - Java Spring Boot

A comprehensive RESTful API for managing hotel rooms and room types built with Java Spring Boot, Hibernate, and PostgreSQL/H2.

## 🏨 Features

### Room Type Management
- Create, read, update, and delete room types
- Search room types by name or description
- Filter by price range and occupancy
- Get room types with available rooms
- Activate/deactivate room types

### Room Management
- Complete CRUD operations for rooms
- Advanced filtering and search capabilities
- Room status management (Available, Occupied, Maintenance, Out of Order)
- Room statistics and reporting
- Floor-based room organization
- Maintenance tracking

### API Features
- RESTful API design following OpenAPI standards
- Comprehensive Swagger/OpenAPI documentation
- Input validation and error handling
- Pagination support for large datasets
- Detailed logging and monitoring

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (development), PostgreSQL (production)
- **ORM**: Hibernate/JPA
- **Documentation**: SpringDoc OpenAPI 3
- **Build Tool**: Maven
- **Validation**: Bean Validation (JSR-303)
- **Mapping**: MapStruct
- **Logging**: SLF4J with Logback

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hotel-room-management
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Documentation: http://localhost:8080/hotel-management/swagger-ui.html
   - H2 Console: http://localhost:8080/hotel-management/h2-console
   - API Base URL: http://localhost:8080/hotel-management/api/v1

### Database Configuration

#### Development (H2)
The application uses H2 in-memory database by default for development:
- URL: `jdbc:h2:mem:hoteldb`
- Username: `sa`
- Password: `password`

#### Production (PostgreSQL)
For production, configure PostgreSQL in `application.yml`:
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:postgresql://localhost:5432/hotel_management
    username: ${DB_USERNAME:hotel_user}
    password: ${DB_PASSWORD:hotel_password}
```

## 📚 API Documentation

### Room Types Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/room-types` | Get all room types (paginated) |
| GET | `/api/v1/room-types/active` | Get active room types |
| GET | `/api/v1/room-types/{id}` | Get room type by ID |
| POST | `/api/v1/room-types` | Create new room type |
| PUT | `/api/v1/room-types/{id}` | Update room type |
| DELETE | `/api/v1/room-types/{id}` | Delete room type |
| GET | `/api/v1/room-types/search?q={term}` | Search room types |
| GET | `/api/v1/room-types/price-range` | Filter by price range |
| GET | `/api/v1/room-types/occupancy` | Filter by occupancy |
| GET | `/api/v1/room-types/available` | Get types with available rooms |
| PATCH | `/api/v1/room-types/{id}/toggle-status` | Toggle room type status |

### Rooms Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/rooms` | Get all rooms (paginated) |
| GET | `/api/v1/rooms/active` | Get active rooms |
| GET | `/api/v1/rooms/{id}` | Get room by ID |
| GET | `/api/v1/rooms/number/{roomNumber}` | Get room by number |
| POST | `/api/v1/rooms` | Create new room |
| PUT | `/api/v1/rooms/{id}` | Update room |
| PATCH | `/api/v1/rooms/{id}/status` | Update room status |
| DELETE | `/api/v1/rooms/{id}` | Delete room |
| POST | `/api/v1/rooms/search` | Search rooms with filters |
| GET | `/api/v1/rooms/available` | Get available rooms |
| GET | `/api/v1/rooms/available/type/{typeId}` | Get available rooms by type |
| GET | `/api/v1/rooms/status/{status}` | Get rooms by status |
| GET | `/api/v1/rooms/floor/{floor}` | Get rooms by floor |
| GET | `/api/v1/rooms/maintenance-needed` | Get rooms needing maintenance |
| GET | `/api/v1/rooms/statistics` | Get room statistics |
| PATCH | `/api/v1/rooms/{id}/toggle-status` | Toggle room status |

## 🏗️ Project Structure

```
src/main/java/com/hotel/roommanagement/
├── HotelRoomManagementApplication.java    # Main application class
├── controller/                            # REST controllers
│   ├── RoomController.java
│   └── RoomTypeController.java
├── dto/                                   # Data Transfer Objects
│   ├── RoomDto.java
│   └── RoomTypeDto.java
├── entity/                                # JPA entities
│   ├── Room.java
│   └── RoomType.java
├── enums/                                 # Enumerations
│   └── RoomStatus.java
├── exception/                             # Exception handling
│   ├── BusinessLogicException.java
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── mapper/                                # MapStruct mappers
│   ├── RoomMapper.java
│   └── RoomTypeMapper.java
├── repository/                            # JPA repositories
│   ├── RoomRepository.java
│   └── RoomTypeRepository.java
└── service/                               # Business logic services
    ├── RoomService.java
    └── RoomTypeService.java

src/main/resources/
├── application.yml                        # Application configuration
└── data.sql                              # Sample data
```

## 🔧 Configuration

### Application Profiles

- **Default**: Development profile with H2 database
- **dev**: Development profile with detailed logging
- **prod**: Production profile with PostgreSQL

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_USERNAME` | Database username | `hotel_user` |
| `DB_PASSWORD` | Database password | `hotel_password` |
| `SERVER_PORT` | Server port | `8080` |

## 🧪 Testing

Run tests with Maven:
```bash
mvn test
```

## 📊 Sample Data

The application includes sample data with:
- 4 room types (Standard, Deluxe, Suite, Presidential Suite)
- 20 rooms across 5 floors
- Various room statuses and configurations

## 🔍 Monitoring

The application includes Spring Boot Actuator endpoints:
- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`

## 🚀 Deployment

### Docker Deployment
```bash
# Build the application
mvn clean package

# Run with Docker
docker run -p 8080:8080 hotel-room-management:latest
```

### Production Deployment
1. Set the active profile to `prod`
2. Configure PostgreSQL database
3. Set environment variables for database credentials
4. Build and deploy the JAR file

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support and questions, please contact the Hotel Management Team.

---

**Hotel Room Management System** - Built with ❤️ using Java Spring Boot