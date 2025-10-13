package com.innowise.repository;

import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUser(AuthUser user);

    void deleteAllByExpiryDateBefore(Instant expiryDateBefore);
}
