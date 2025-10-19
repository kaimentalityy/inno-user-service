package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dao.repository.OrderItemRepository;
import com.innowise.orderservice.dao.specification.OrderItemSpecifications;
import com.innowise.orderservice.exception.OrderItemNotFoundException;
import com.innowise.orderservice.mapper.OrderItemMapper;
import com.innowise.orderservice.model.dto.OrderItemDto;
import com.innowise.orderservice.model.entity.OrderItem;
import com.innowise.orderservice.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderItemDto create(OrderItemDto createDto) {
        OrderItem orderItem = orderItemMapper.toEntity(createDto);
        OrderItem saved = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderItemDto update(Long id, OrderItemDto updateDto) {
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException());

        orderItemMapper.updateEntity(existing, updateDto);
        OrderItem updated = orderItemRepository.save(existing);
        return orderItemMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException();
        }
        orderItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException());
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemDto> searchOrderItems(Long orderId, Long itemId, Integer quantity, Integer minQuantity, Integer maxQuantity, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(null);

        if (orderId != null) spec = spec.and(OrderItemSpecifications.hasOrderId(orderId));
        if (itemId != null) spec = spec.and(OrderItemSpecifications.hasItemId(itemId));
        if (quantity != null) spec = spec.and(OrderItemSpecifications.hasQuantity(quantity));
        if (minQuantity != null) spec = spec.and(OrderItemSpecifications.quantityGreaterThan(minQuantity));
        if (maxQuantity != null) spec = spec.and(OrderItemSpecifications.quantityLessThan(maxQuantity));

        return orderItemRepository.findAll(spec, pageable)
                .map(orderItemMapper::toDto);
    }

}
