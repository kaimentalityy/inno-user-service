package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dao.repository.OrderItemRepository;
import com.innowise.orderservice.dao.specification.OrderItemSpecifications;
import com.innowise.orderservice.mapper.OrderItemMapper;
import com.innowise.orderservice.model.dto.OrderItemDto;
import com.innowise.orderservice.model.entity.OrderItem;
import com.innowise.orderservice.service.CrudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements CrudService<OrderItemDto, Long> {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDto create(OrderItemDto createDto) {
        OrderItem orderItem = orderItemMapper.toEntity(createDto);
        OrderItem saved = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(saved);
    }

    @Override
    public OrderItemDto update(Long id, OrderItemDto updateDto) {
        OrderItem existing = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem with id " + id + " not found"));

        orderItemMapper.updateEntity(existing, updateDto);
        OrderItem updated = orderItemRepository.save(existing);
        return orderItemMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new EntityNotFoundException("OrderItem with id " + id + " not found");
        }
        orderItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem with id " + id + " not found"));
        return orderItemMapper.toDto(orderItem);
    }

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
