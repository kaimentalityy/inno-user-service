package com.innowise.userservice.presentation.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CardInfoDto(
        Long id,

        @NotNull
        Long userId,

        @NotNull
        String cardNumber,

        @NotNull
        String cardHolder,

        @FutureOrPresent
        LocalDate cardExpiryDate
) {}
