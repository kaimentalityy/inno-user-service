package com.innowise.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.time.Instant;

/**
 * Entity representing a refresh token for JWT authentication.
 * <p>
 * Each token is linked to a specific user and has an expiration date.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {

    /** Primary key for the refresh token entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User associated with this refresh token. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;

    /** The token value, must be unique. */
    @Column(unique = true, nullable = false)
    private String token;

    /** Expiration date for this refresh token. */
    @Column(nullable = false)
    private Instant expiryDate;
}
