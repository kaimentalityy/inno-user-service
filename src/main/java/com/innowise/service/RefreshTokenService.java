package com.innowise.service;

import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;

/**
 * Service for managing refresh tokens.
 */
public interface RefreshTokenService {

    /**
     * Creates a new refresh token for the given user.
     *
     * @param user the user
     * @return the created refresh token
     */
    RefreshToken createRefreshToken(AuthUser user);

    /**
     * Validates the refresh token.
     *
     * @param token the refresh token string
     * @return true if valid, false otherwise
     */
    boolean validateRefreshToken(String token);

    /**
     * Retrieves the user associated with the given refresh token.
     *
     * @param token the refresh token string
     * @return the user
     */
    AuthUser getUserFromRefreshToken(String token);

    /**
     * Deletes all existing refresh tokens for a user.
     *
     * @param user the user
     */
    void deleteRefreshTokensForUser(AuthUser user);
}
