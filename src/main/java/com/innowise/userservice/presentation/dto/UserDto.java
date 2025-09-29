package com.innowise.userservice.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record UserDto(
        Long id,

        @NotNull
        String name,

        @NotNull
        String surname,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotNull
        String email,

        List<CardInfoDto> cards
) {}
