package com.innowise.controller;

import com.innowise.model.dto.AuthResponseDTO;
import com.innowise.model.dto.AuthDto;
import com.innowise.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for user authentication and token management.
 * <p>
 * Provides endpoints for registering users, logging in, refreshing tokens,
 * and validating JWT access tokens.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user with the provided credentials.
     *
     * @param request the registration data containing username and password
     * @return {@link AuthResponseDTO} containing access token, refresh token, and username
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid AuthDto request) {
        AuthResponseDTO resp = authService.register(request);
        return ResponseEntity.ok(resp);
    }

    /**
     * Authenticates an existing user and returns tokens.
     *
     * @param request the login data containing username and password
     * @return {@link AuthResponseDTO} containing access token, refresh token, and username
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthDto request) {
        AuthResponseDTO resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token
     * @return a map containing the new access token and the same refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestParam String refreshToken) {
        Map<String, String> map = authService.refresh(refreshToken);
        return ResponseEntity.ok(map);
    }

    /**
     * Validates a JWT access token.
     *
     * @param token the JWT access token to validate
     * @return a map with a single entry "valid" set to true if the token is valid, false otherwise
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validate(@RequestParam String token) {
        boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}
