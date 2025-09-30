package com.innowise.orderservice.mapper;

import com.innowise.orderservice.data.entity.Order;
import com.innowise.orderservice.data.entity.OrderItem;
import com.innowise.orderservice.dto.OrderDto;
import com.innowise.orderservice.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "userId", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "userId")
    Order toEntity(OrderDto dto);
}
