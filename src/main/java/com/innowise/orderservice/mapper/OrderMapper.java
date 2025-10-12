package com.innowise.orderservice.mapper;

import com.innowise.orderservice.model.dto.OrderDto;
import com.innowise.orderservice.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "userId", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "userId")
    Order toEntity(OrderDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Order entity, OrderDto dto);
}
