package com.hotel.roommanagement.mapper;

import com.hotel.roommanagement.dto.RoomDto;
import com.hotel.roommanagement.entity.Room;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Room entity and DTOs
 */
@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoomTypeMapper.class})
public interface RoomMapper {

    /**
     * Convert CreateRequest to Entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "lastMaintenance", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    Room toEntity(RoomDto.CreateRequest request);

    /**
     * Convert Entity to Response
     */
    @Mapping(target = "currentPrice", expression = "java(room.getCurrentPrice())")
    @Mapping(target = "displayName", expression = "java(room.getDisplayName())")
    RoomDto.Response toResponse(Room room);

    /**
     * Convert Entity to ListItem
     */
    @Mapping(target = "roomTypeName", source = "roomType.name")
    @Mapping(target = "currentPrice", expression = "java(room.getCurrentPrice())")
    @Mapping(target = "displayName", expression = "java(room.getDisplayName())")
    RoomDto.ListItem toListItem(Room room);

    /**
     * Convert list of entities to list of ListItems
     */
    List<RoomDto.ListItem> toListItems(List<Room> rooms);

    /**
     * Update entity from UpdateRequest
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roomType", ignore = true)
    @Mapping(target = "lastMaintenance", ignore = true)
    void updateEntity(@MappingTarget Room room, RoomDto.UpdateRequest request);
}