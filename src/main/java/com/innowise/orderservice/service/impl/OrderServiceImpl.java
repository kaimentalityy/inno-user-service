package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.client.UserServiceClient;
import com.innowise.orderservice.dao.repository.OrderRepository;
import com.innowise.orderservice.dao.specification.OrderSpecifications;
import com.innowise.orderservice.exception.OrderNotFoundException;
import com.innowise.orderservice.mapper.OrderMapper;
import com.innowise.orderservice.model.dto.OrderDto;
import com.innowise.orderservice.model.dto.UserInfoDto;
import com.innowise.orderservice.model.entity.Order;
import com.innowise.orderservice.service.OrderService;
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
    @Transactional
    public OrderDto create(OrderDto createDto) {
        Order order = orderMapper.toEntity(createDto);
        Order saved = orderRepository.save(order);

        UserInfoDto userInfo = fetchUserInfo(order.getUserId(), null);
        return new OrderDto(
                saved.getId(),
                saved.getUserId(),
                saved.getStatus(),
                saved.getCreatedDate(),
                orderMapper.orderItemsToDtos(saved.getItems()),
                userInfo
        );
    }

    @Override
    @Transactional
    public OrderDto update(Long id, OrderDto updateDto) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());

        orderMapper.updateEntity(existing, updateDto);
        Order updated = orderRepository.save(existing);

        UserInfoDto userInfo = fetchUserInfo(updated.getUserId(), null);
        return new OrderDto(
                updated.getId(),
                updated.getUserId(),
                updated.getStatus(),
                updated.getCreatedDate(),
                orderMapper.orderItemsToDtos(updated.getItems()),
                userInfo
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());

        UserInfoDto userInfo = fetchUserInfo(order.getUserId(), null);
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getCreatedDate(),
                orderMapper.orderItemsToDtos(order.getItems()),
                userInfo
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> searchOrders(Long userId, String email, String status,
                                       LocalDateTime createdAfter, LocalDateTime createdBefore,
                                       Pageable pageable) {

        Specification<Order> spec = Specification.where(null);

        if (userId != null) spec = spec.and(OrderSpecifications.hasUserId(userId));
        if (status != null) spec = spec.and(OrderSpecifications.hasStatus(status));
        if (createdAfter != null) spec = spec.and(OrderSpecifications.createdAfter(createdAfter));
        if (createdBefore != null) spec = spec.and(OrderSpecifications.createdBefore(createdBefore));

        Page<Order> orders = orderRepository.findAll(spec, pageable);

        return orders.map(order -> {
            UserInfoDto userInfo = fetchUserInfo(order.getUserId(), email);
            return new OrderDto(
                    order.getId(),
                    order.getUserId(),
                    order.getStatus(),
                    order.getCreatedDate(),
                    orderMapper.orderItemsToDtos(order.getItems()),
                    userInfo
            );
        });
    }

    private UserInfoDto fetchUserInfo(Long userId, String email) {
        if (email != null && !email.isEmpty()) {
            return userServiceClient.getUserByEmail(email);
        }
        return userServiceClient.getUserById(userId);
    }
}

