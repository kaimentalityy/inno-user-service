package com.innowise.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "MySuperSecretKeyForJWTMySuperSecretKeyForJWT");
    }

    private String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(Map.of("role", role))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
                .signWith(SignatureAlgorithm.HS256, "MySuperSecretKeyForJWTMySuperSecretKeyForJWT".getBytes())
                .compact();
    }

    @Test
    void shouldValidateValidToken() {
        String token = generateToken("user", "ADMIN");
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void shouldInvalidateBrokenToken() {
        assertFalse(jwtTokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void shouldExtractUsername() {
        String token = generateToken("john", "USER");
        assertEquals("john", jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void shouldExtractRole() {
        String token = generateToken("mike", "ADMIN");
        assertEquals("ADMIN", jwtTokenProvider.getRoleFromToken(token));
    }
}
