package com.innowise.orderservice.dto;

import com.innowise.orderservice.data.entity.OrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    private Long id;

    @NotNull
    private String status;

    @NotNull
    private String userId;

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private List<OrderItem> items;
}
