package com.innowise.orderservice.mapper;

import com.innowise.orderservice.data.entity.OrderItem;
import com.innowise.orderservice.dto.OrderItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "order.id", target = "orderId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "itemId", target = "item.id")
    @Mapping(target = "order.id", source = "orderId")
    OrderItem toEntity(OrderItemDto dto);
}
