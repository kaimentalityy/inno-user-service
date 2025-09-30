package com.innowise.orderservice.mapper;

import com.innowise.orderservice.data.entity.Items;
import com.innowise.orderservice.dto.ItemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toDto(Items entity);

    Items toEntity(ItemDto dto);

    List<ItemDto> toDtoList(List<Items> entities);
    List<Items> toEntityList(List<ItemDto> dtoList);
}
