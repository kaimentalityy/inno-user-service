package com.innowise.orderservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    private Long id;

    @NotNull(message = "Status cannot be null")
    private String status;

    @NotNull(message = "User ID cannot be null")
    private String userId;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime createdDate;

    private List<OrderItemDto> items;

    private UserInfoDto userInfo;
}
