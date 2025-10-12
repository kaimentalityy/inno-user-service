package com.innowise.orderservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

}
