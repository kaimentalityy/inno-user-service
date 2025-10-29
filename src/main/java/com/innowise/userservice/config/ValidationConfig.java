package com.innowise.userservice.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for validation and internationalization setup.
 * Configures message source for validation messages and integrates
 * with Bean Validation API for custom validation error messages.
 *
 * @author Your Name
 * @version 1.0
 * @since 2024
 *
 * @see MessageSource
 * @see LocalValidatorFactoryBean
 */
@Configuration
public class ValidationConfig {

    /**
     * Configures the message source for validation messages.
     * Sets up a ResourceBundleMessageSource that loads validation messages
     * from property files named "ValidationMessages" with UTF-8 encoding.
     * This allows for internationalized validation error messages.
     *
     * @return MessageSource configured to read from ValidationMessages.properties files
     *
     * @see ResourceBundleMessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("ValidationMessages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    /**
     * Configures the validator factory bean with custom message source.
     * Integrates the message source with Bean Validation to provide
     * localized validation error messages from property files.
     *
     * @param messageSource the message source bean providing validation messages
     * @return LocalValidatorFactoryBean configured with the custom message source
     *
     * @see LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }
}
