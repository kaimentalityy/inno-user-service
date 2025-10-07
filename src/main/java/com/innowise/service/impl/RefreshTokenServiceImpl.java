package com.innowise.service.impl;

import com.innowise.exception.InvalidTokenException;
import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;
import com.innowise.repository.RefreshTokenRepo;
import com.innowise.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    @Override
    public RefreshToken createRefreshToken(AuthUser user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        return refreshTokenRepo.save(token);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findByToken(token);
        return refreshToken.isPresent() && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }

    @Override
    public AuthUser getUserFromRefreshToken(String token) {
        return refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token Not Found"))
                .getUser();
    }
}
