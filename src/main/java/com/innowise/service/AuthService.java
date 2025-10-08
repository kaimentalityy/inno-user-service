package com.innowise.service;

import com.innowise.model.dto.resp.AuthResponseDTO;
import com.innowise.model.dto.rq.AuthDto;

import java.util.Map;

/**
 * Handles user authentication and token management.
 */
public interface AuthService {

    /**
     * Registers a new user and returns tokens.
     *
     * @param request registration data
     * @return auth response with tokens
     */
    AuthResponseDTO register(AuthDto request);

    /**
     * Logs in an existing user.
     *
     * @param request login data
     * @return auth response with tokens
     */
    AuthResponseDTO login(AuthDto request);

    /**
     * Refreshes the access token.
     *
     * @param refreshToken existing refresh token
     * @return new access and refresh tokens
     */
    Map<String, String> refresh(String refreshToken);
}
