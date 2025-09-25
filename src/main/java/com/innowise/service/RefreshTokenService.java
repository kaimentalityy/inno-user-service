package com.innowise.service;

import com.innowise.data.entity.AuthUser;
import com.innowise.data.entity.RefreshToken;
import com.innowise.data.repository.RefreshTokenRepo;
import com.innowise.data.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing refresh tokens.
 * <p>
 * Responsible for creating, validating, and retrieving information
 * from refresh tokens used for JWT authentication.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    /** Repository for accessing refresh token data. */
    private final RefreshTokenRepo refreshTokenRepo;

    /** Repository for accessing user data. */
    private final UserRepo userRepo;

    /**
     * Creates a new refresh token for the given username.
     * <p>
     * The token is valid for 7 days by default.
     *
     * @param username the username for which the refresh token is created
     * @return the created {@link RefreshToken} entity
     */
    public RefreshToken createRefreshToken(String username) {
        AuthUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        return refreshTokenRepo.save(token);
    }

    /**
     * Validates a given refresh token.
     * <p>
     * A token is valid if it exists and has not expired.
     *
     * @param token the refresh token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findByToken(token);
        return refreshToken.isPresent() && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }

    /**
     * Retrieves the username associated with a refresh token.
     *
     * @param token the refresh token
     * @return the username of the user linked to the token
     * @throws RuntimeException if the token is not found
     */
    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"))
                .getUser()
                .getUsername();
    }

}
