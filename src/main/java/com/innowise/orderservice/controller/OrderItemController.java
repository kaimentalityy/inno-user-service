package com.innowise.orderservice.controller;

import com.innowise.orderservice.model.dto.OrderItemDto;
import com.innowise.orderservice.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItemDto> create(@Valid @RequestBody OrderItemDto dto) {
        OrderItemDto created = orderItemService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> update(@PathVariable Long id, @Valid @RequestBody OrderItemDto dto) {
        OrderItemDto updated = orderItemService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getById(@PathVariable Long id) {
        OrderItemDto dto = orderItemService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<OrderItemDto>> search(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<OrderItemDto> results = orderItemService.searchOrderItems(orderId, itemId, quantity, minQuantity, maxQuantity, pageRequest);
        return ResponseEntity.ok(results);
    }
}
