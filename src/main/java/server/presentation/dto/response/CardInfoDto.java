package server.presentation.dto.response;

import java.time.LocalDate;

public record CardInfoDto (Long  id, String cardNumber, String cardHolder, LocalDate cardExpiryDate) {
}
