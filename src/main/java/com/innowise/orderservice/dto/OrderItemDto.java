package com.innowise.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDto {

    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Long itemId;

    @NotNull
    private Integer quantity;
}
