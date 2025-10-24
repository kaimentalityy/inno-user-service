package com.innowise.orderservice.service;

import com.innowise.orderservice.model.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing {@link ItemDto} entities.
 * Provides CRUD operations and search with filters and pagination.
 */
public interface ItemService extends CrudService<ItemDto, Long> {

    /**
     * Searches items with optional filters.
     *
     * @param name      partial match on item name (nullable)
     * @param price     filter by price (nullable)
     * @param exactName exact match on item name (nullable)
     * @param pageable  pagination settings
     * @return page of matching {@link ItemDto}
     */
    Page<ItemDto> searchItems(String name, String price, String exactName, Pageable pageable);
}
