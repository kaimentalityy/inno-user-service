package server.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateUserDto(

        @NotNull
        String name,

        @NotNull
        String surname,

        @NotNull
        @Past
        LocalDate birthDate,

        @NotNull
        String email) {}
