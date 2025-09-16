package com.innowise.userservice.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateCardInfoDto(

        @NotNull
        Long id,

        @NotNull
        String cardNumber,

        @NotNull
        String cardHolder,

        @FutureOrPresent
        LocalDate cardExpiryDate) {}
