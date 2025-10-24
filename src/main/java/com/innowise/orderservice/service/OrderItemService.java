package com.innowise.orderservice.service;

import com.innowise.orderservice.model.dto.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing {@link OrderItemDto} entities.
 * Extends basic CRUD operations and provides search with filters and pagination.
 */
public interface OrderItemService extends CrudService<OrderItemDto, Long> {

    /**
     * Finds order items using optional filters.
     *
     * @param orderId     filter by order ID (nullable)
     * @param itemId      filter by item ID (nullable)
     * @param quantity    exact quantity (nullable)
     * @param minQuantity minimum quantity (nullable)
     * @param maxQuantity maximum quantity (nullable)
     * @param pageable    pagination settings
     * @return page of matching {@link OrderItemDto}
     */
    Page<OrderItemDto> searchOrderItems(Long orderId,
                                        Long itemId,
                                        Integer quantity,
                                        Integer minQuantity,
                                        Integer maxQuantity,
                                        Pageable pageable);
}
