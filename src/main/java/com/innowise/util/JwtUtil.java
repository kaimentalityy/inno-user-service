package com.innowise.util;

import com.innowise.data.entity.AuthUser;
import com.innowise.data.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for generating and validating JWT tokens.
 * Handles token creation, extracting username and roles, and validation.
 */
@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    /**
     * Constructor initializes signing key and expiration time from application properties.
     *
     * @param secret       JWT secret key
     * @param expirationMs Expiration time in milliseconds
     */
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param authUser User entity containing username and roles
     * @return Signed JWT token
     */
    public String generateToken(AuthUser authUser) {

        Set<String> roles = authUser.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(authUser.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token
     * @return Username contained in the token
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Extracts the roles from a JWT token.
     *
     * @param token JWT token
     * @return List of role names
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", List.class);
    }

    /**
     * Validates a JWT token by verifying its signature and expiration.
     *
     * @param token JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
