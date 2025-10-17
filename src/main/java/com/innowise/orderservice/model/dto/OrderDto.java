package com.innowise.orderservice.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,

        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotBlank(message = "Status cannot be blank")
        String status,

        @FutureOrPresent(message = "Creation date must be in the present or future")
        LocalDateTime createdDate,

        @Valid
        List<OrderItemDto> items,

        UserInfoDto userInfo
) {}


