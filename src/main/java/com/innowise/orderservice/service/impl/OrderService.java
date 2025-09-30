package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dto.OrderItemDto;
import com.innowise.orderservice.service.CrudService;

public class OrderService implements CrudService<OrderItemDto, Long> {


    @Override
    public OrderItemDto create(OrderItemDto createDto) {
        return null;
    }

    @Override
    public OrderItemDto update(Long aLong, OrderItemDto updateDto) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public OrderItemDto findById(Long aLong) {
        return null;
    }
}
