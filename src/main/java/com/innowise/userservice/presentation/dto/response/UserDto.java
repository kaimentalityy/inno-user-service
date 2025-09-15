package com.innowise.userservice.presentation.dto.response;

import java.time.LocalDate;
import java.util.List;

public record UserDto(Long id, String name, String surname, LocalDate birthdate, String email, List<CardInfoDto> cards) {
}
