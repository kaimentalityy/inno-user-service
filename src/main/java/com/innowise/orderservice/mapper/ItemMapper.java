package com.innowise.orderservice.mapper;

import com.innowise.orderservice.model.dto.ItemDto;
import com.innowise.orderservice.model.entity.Items;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toDto(Items entity);

    Items toEntity(ItemDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Items entity, ItemDto dto);
}
