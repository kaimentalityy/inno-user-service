package com.innowise.userservice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record CreateUserDto (

        @NotNull
        String name,

        @NotNull
        String surname,

        @NotNull
        @Past
        LocalDate birthdate,

        @NotNull
        String email,

        List<CreateCardInfoDto> cards) {
}
