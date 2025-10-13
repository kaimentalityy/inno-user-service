package com.innowise.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.exception.PasswordEncodingException;
import com.innowise.exception.PasswordVerificationException;
import com.innowise.util.ExceptionMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Adapter between Spring Security and our PasswordHash model without manual concatenation.
 */
public class CustomSpringPasswordEncoder implements PasswordEncoder {

    private final CustomPasswordEncoder encoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomSpringPasswordEncoder(CustomPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            PasswordHash hash = encoder.encode(rawPassword.toString());
            String json = objectMapper.writeValueAsString(hash);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new PasswordEncodingException(ExceptionMessage.PASSWORD_ENCODING.get());
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String json = new String(Base64.getDecoder().decode(encodedPassword), StandardCharsets.UTF_8);
            PasswordHash storedHash = objectMapper.readValue(json, PasswordHash.class);
            return encoder.matches(rawPassword.toString(), storedHash);
        } catch (Exception e) {
            throw new PasswordVerificationException(ExceptionMessage.PASSWORD_VERIFICATION.get());
        }
    }
}
