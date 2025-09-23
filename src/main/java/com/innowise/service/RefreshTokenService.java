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

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserRepo userRepo;

    public RefreshToken createRefreshToken(String username) {
        AuthUser user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        return refreshTokenRepo.save(token);
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findByToken(token);
        return refreshToken.isPresent() && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }

    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"))
                .getUser()
                .getUsername();
    }

}
