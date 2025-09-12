package server.presentation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCardInfoDto(

        @NotNull
        Long userId,

        @NotNull
        String cardNumber,

        @NotNull
        String cardHolderName,

        @NotNull
        @FutureOrPresent
        LocalDate cardExpiryDate
) {}
