package com.innowise.service.impl;

import com.innowise.exception.InvalidTokenException;
import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;
import com.innowise.repository.RefreshTokenRepository;
import com.innowise.service.RefreshTokenService;
import com.innowise.util.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link RefreshTokenService}.
 * Manages creation, validation, and deletion of refresh tokens.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(AuthUser user) {
        deleteRefreshTokensForUser(user);

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS));
        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        return refreshToken.isPresent() && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }

    @Override
    public AuthUser getUserFromRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(ExceptionMessage.INVALID_TOKEN.get()))
                .getUser();
    }

    @Scheduled(fixedDelay = 3600000)
    public void cleanupExpiredTokens() {
        int deleted = refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
        if (deleted > 0) {
            log.info("Deleted {} expired refresh tokens", deleted);
        }
    }


    @Override
    public void deleteRefreshTokensForUser(AuthUser user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}
