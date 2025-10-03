package com.innowise.userservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record UserDto(
        Long id,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 100)
        String surname,

        @Past(message = "Birthdate must be in the past")
        LocalDate birthDate,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        List<CardInfoDto> cards
) {}
