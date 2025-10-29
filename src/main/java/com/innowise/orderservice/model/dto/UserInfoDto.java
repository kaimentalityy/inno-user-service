package com.innowise.orderservice.model.dto;

import jakarta.validation.constraints.*;

public record UserInfoDto(
        @NotNull(message = "User ID cannot be null")
        Long id,

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotBlank(message = "Surname cannot be blank")
        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        String surname,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email
) {}
