package com.innowise.userservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class ValidationConfigTest {

    private final ValidationConfig config = new ValidationConfig();

    @Test
    void testMessageSourceBean() {
        MessageSource messageSource = config.messageSource();
        assertNotNull(messageSource);
    }

    @Test
    void testValidatorBean() {
        LocalValidatorFactoryBean validator = config.getValidator(config.messageSource());
        assertNotNull(validator);
    }
}
