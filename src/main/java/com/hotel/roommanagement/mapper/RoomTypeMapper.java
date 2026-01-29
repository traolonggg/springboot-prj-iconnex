package com.hotel.roommanagement.mapper;

import com.hotel.roommanagement.dto.RoomTypeDto;
import com.hotel.roommanagement.entity.RoomType;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for RoomType entity and DTOs
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomTypeMapper {

    /**
     * Convert CreateRequest to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    RoomType toEntity(RoomTypeDto.CreateRequest request);

    /**
     * Convert Entity to Response
     */
    @Mapping(target = "roomCount", expression = "java(roomType.getRoomCount())")
    @Mapping(target = "activeRoomCount", expression = "java(roomType.getActiveRoomCount())")
    RoomTypeDto.Response toResponse(RoomType roomType);

    /**
     * Convert Entity to ListItem
     */
    @Mapping(target = "roomCount", expression = "java(roomType.getRoomCount())")
    RoomTypeDto.ListItem toListItem(RoomType roomType);

    /**
     * Convert list of entities to list of ListItems
     */
    List<RoomTypeDto.ListItem> toListItems(List<RoomType> roomTypes);

    /**
     * Update entity from UpdateRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    void updateEntity(@MappingTarget RoomType roomType, RoomTypeDto.UpdateRequest request);
}