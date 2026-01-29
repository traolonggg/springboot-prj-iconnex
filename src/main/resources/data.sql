-- Sample data for Hotel Room Management System

-- Insert Room Types
INSERT INTO room_types (name, description, base_price, max_occupancy, size_sqm, amenities, image_url, is_active, created_at, updated_at) VALUES
('Standard Room', 'Comfortable room with basic amenities', 100.00, 2, 25, '["Air Conditioning", "TV", "WiFi", "Private Bathroom"]', 'https://example.com/images/standard-room.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Deluxe Room', 'Spacious room with premium amenities', 150.00, 3, 35, '["Air Conditioning", "TV", "WiFi", "Mini Bar", "Room Service", "Balcony"]', 'https://example.com/images/deluxe-room.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Suite', 'Luxury suite with separate living area', 250.00, 4, 60, '["Air Conditioning", "TV", "WiFi", "Mini Bar", "Room Service", "Balcony", "Living Area", "Kitchenette"]', 'https://example.com/images/suite.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Presidential Suite', 'Ultimate luxury accommodation', 500.00, 6, 120, '["Air Conditioning", "TV", "WiFi", "Mini Bar", "Room Service", "Balcony", "Living Area", "Full Kitchen", "Jacuzzi", "Butler Service"]', 'https://example.com/images/presidential-suite.jpg', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Rooms
-- Floor 1 - Standard Rooms
INSERT INTO rooms (room_number, room_type_id, floor, status, view_type, has_balcony, wifi_password, notes, is_active, created_at, updated_at) VALUES
('101', 1, 1, 'AVAILABLE', 'Garden view', false, 'hotel101', 'Near reception area', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('102', 1, 1, 'AVAILABLE', 'Garden view', false, 'hotel102', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('103', 1, 1, 'OCCUPIED', 'Garden view', false, 'hotel103', 'Guest checked in today', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('104', 1, 1, 'AVAILABLE', 'Garden view', false, 'hotel104', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('105', 1, 1, 'MAINTENANCE', 'Garden view', false, 'hotel105', 'AC repair needed', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Floor 2 - Deluxe Rooms
INSERT INTO rooms (room_number, room_type_id, floor, status, view_type, has_balcony, wifi_password, notes, is_active, created_at, updated_at) VALUES
('201', 2, 2, 'AVAILABLE', 'Sea view', true, 'hotel201', 'Corner room with panoramic view', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('202', 2, 2, 'AVAILABLE', 'Sea view', true, 'hotel202', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('203', 2, 2, 'OCCUPIED', 'Sea view', true, 'hotel203', 'VIP guest', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('204', 2, 2, 'AVAILABLE', 'City view', true, 'hotel204', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('205', 2, 2, 'AVAILABLE', 'City view', true, 'hotel205', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Floor 3 - Mix of Deluxe and Suites
INSERT INTO rooms (room_number, room_type_id, floor, status, view_type, has_balcony, wifi_password, notes, is_active, created_at, updated_at) VALUES
('301', 2, 3, 'AVAILABLE', 'Sea view', true, 'hotel301', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('302', 3, 3, 'AVAILABLE', 'Sea view', true, 'hotel302', 'Honeymoon suite', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('303', 3, 3, 'OCCUPIED', 'Sea view', true, 'hotel303', 'Business traveler', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('304', 2, 3, 'AVAILABLE', 'City view', true, 'hotel304', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Floor 4 - Premium Suites
INSERT INTO rooms (room_number, room_type_id, floor, status, view_type, has_balcony, wifi_password, notes, is_active, created_at, updated_at) VALUES
('401', 3, 4, 'AVAILABLE', 'Sea view', true, 'hotel401', 'Executive suite', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('402', 3, 4, 'AVAILABLE', 'Sea view', true, 'hotel402', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('403', 4, 4, 'AVAILABLE', 'Panoramic view', true, 'hotel403', 'Presidential suite with private elevator', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Floor 5 - Standard and Deluxe mix
INSERT INTO rooms (room_number, room_type_id, floor, status, view_type, has_balcony, wifi_password, notes, is_active, created_at, updated_at) VALUES
('501', 1, 5, 'AVAILABLE', 'City view', false, 'hotel501', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('502', 1, 5, 'OUT_OF_ORDER', 'City view', false, 'hotel502', 'Plumbing issues - under repair', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('503', 2, 5, 'AVAILABLE', 'City view', true, 'hotel503', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('504', 2, 5, 'AVAILABLE', 'City view', true, 'hotel504', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('505', 1, 5, 'AVAILABLE', 'City view', false, 'hotel505', null, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);