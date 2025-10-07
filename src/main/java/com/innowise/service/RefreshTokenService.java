package com.innowise.service;

import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;

public interface RefreshTokenService {

    /**
     * Creates a new refresh token for the given user.
     *
     * @param user the user for whom the token is created
     * @return the created {@link RefreshToken}
     */
    RefreshToken createRefreshToken(AuthUser user);

    /**
     * Validates a given refresh token.
     *
     * @param token the refresh token to validate
     * @return true if valid, false otherwise
     */
    boolean validateRefreshToken(String token);

    /**
     * Retrieves the user associated with the refresh token.
     *
     * @param token the refresh token
     * @return the corresponding {@link AuthUser}
     */
    AuthUser getUserFromRefreshToken(String token);
}
