package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.client.UserServiceClient;
import com.innowise.orderservice.dao.repository.OrderRepository;
import com.innowise.orderservice.dao.specification.OrderSpecifications;
import com.innowise.orderservice.mapper.OrderMapper;
import com.innowise.orderservice.model.dto.OrderDto;
import com.innowise.orderservice.model.entity.Order;
import com.innowise.orderservice.service.CrudService;
import com.innowise.orderservice.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserServiceClient userServiceClient;

    @Override
    public OrderDto create(OrderDto createDto) {
        Order order = orderMapper.toEntity(createDto);
        Order saved = orderRepository.save(order);
        OrderDto dto = orderMapper.toDto(saved);

        dto.setUserInfo(userServiceClient.getUserById(order.getUserId()));
        return dto;
    }

    @Override
    public OrderDto update(Long id, OrderDto updateDto) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));

        orderMapper.updateEntity(existing, updateDto);
        Order updated = orderRepository.save(existing);

        OrderDto dto = orderMapper.toDto(updated);
        dto.setUserInfo(userServiceClient.getUserById(updated.getUserId()));
        return dto;
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + id + " not found"));

        OrderDto dto = orderMapper.toDto(order);
        dto.setUserInfo(userServiceClient.getUserById(order.getUserId()));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> searchOrders(String userId, String status, LocalDateTime createdAfter,
                                       LocalDateTime createdBefore, Pageable pageable) {

        Specification<Order> spec = Specification.where(null);

        if (userId != null) spec = spec.and(OrderSpecifications.hasUserId(userId));
        if (status != null) spec = spec.and(OrderSpecifications.hasStatus(status));
        if (createdAfter != null) spec = spec.and(OrderSpecifications.createdAfter(createdAfter));
        if (createdBefore != null) spec = spec.and(OrderSpecifications.createdBefore(createdBefore));

        return orderRepository.findAll(spec, pageable)
                .map(order -> {
                    OrderDto dto = orderMapper.toDto(order);
                    dto.setUserInfo(userServiceClient.getUserById(order.getUserId()));
                    return dto;
                });
    }
}
