package com.innowise.userservice.model.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardInfoDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCardInfoShouldPassValidation() {
        CardInfoDto card = new CardInfoDto(
                1L,
                10L,
                "1234567890123",
                "John Doe",
                LocalDate.now().plusDays(1)
        );

        Set violations = validator.validate(card);
        assertEquals(0, violations.size());
    }

    @Test
    void invalidCardNumberShouldFailValidation() {
        CardInfoDto card = new CardInfoDto(
                1L,
                10L,
                "abc123",
                "John Doe",
                LocalDate.now().plusDays(1)
        );

        Set violations = validator.validate(card);
        assertEquals(1, violations.size());
    }

    @Test
    void pastExpirationDateShouldFailValidation() {
        CardInfoDto card = new CardInfoDto(
                1L,
                10L,
                "1234567890123",
                "John Doe",
                LocalDate.now().minusDays(1)
        );

        Set violations = validator.validate(card);
        assertEquals(1, violations.size());
    }
}
