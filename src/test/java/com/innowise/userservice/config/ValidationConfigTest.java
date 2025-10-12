package com.innowise.userservice.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

class ValidationConfigTest {

    private final ValidationConfig config = new ValidationConfig();

    @Test
    void testMessageSourceBean() {
        MessageSource messageSource = config.messageSource();
        assertNotNull(messageSource, "MessageSource should not be null");
        assertTrue(messageSource instanceof ResourceBundleMessageSource, "MessageSource should be ResourceBundleMessageSource");

        String defaultMessage = "default";
        String resolved = messageSource.getMessage("non.existing.code", null, defaultMessage, null);
        assertEquals(defaultMessage, resolved, "MessageSource should return default message for unknown code");
    }

    @Test
    void testValidatorBean() throws Exception {
        MessageSource messageSource = config.messageSource();
        LocalValidatorFactoryBean validator = config.getValidator(messageSource);
        assertNotNull(validator, "Validator should not be null");

        validator.afterPropertiesSet();

        SampleBean bean = new SampleBean();
        bean.name = "";
        var violations = validator.validate(bean);
        assertFalse(violations.isEmpty(), "Validator should detect violations");
    }


    @Test
    void testValidationConfigConstructor() {
        ValidationConfig validationConfig = new ValidationConfig();
        assertNotNull(validationConfig);
    }

    private static class SampleBean {
        @jakarta.validation.constraints.NotBlank
        String name;
    }
}
