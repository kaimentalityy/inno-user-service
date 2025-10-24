package com.innowise.orderservice.controller;

import com.innowise.orderservice.model.dto.OrderDto;
import com.innowise.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        OrderDto created = orderService.create(orderDto);
        return ResponseEntity
                .created(URI.create("/api/orders/" + created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.update(id, orderDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime createdAfter,
            @RequestParam(required = false) LocalDateTime createdBefore,
            Pageable pageable) {

        return ResponseEntity.ok(
                orderService.searchOrders(userId, email, status, createdAfter, createdBefore, pageable)
        );
    }
}
