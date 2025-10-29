package com.innowise.service.impl;

import com.innowise.exception.AccessDeniedCustomException;
import com.innowise.exception.EntityAlreadyExistsException;
import com.innowise.exception.InvalidTokenException;
import com.innowise.model.dto.AuthResponseDTO;
import com.innowise.model.dto.AuthDto;
import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;
import com.innowise.model.entity.Role;
import com.innowise.repository.RoleRepository;
import com.innowise.repository.UserRepository;
import com.innowise.service.AuthService;
import com.innowise.service.RefreshTokenService;
import com.innowise.util.ExceptionMessage;
import com.innowise.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AuthService}.
 * Handles user registration, login, token refresh, and JWT validation.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * Register: transactional — user save + token creation must be atomic.
     */
    @Override
    @Transactional
    public AuthResponseDTO register(AuthDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new EntityAlreadyExistsException(
                    ExceptionMessage.ENTITY_ALREADY_EXISTS.get()
            );
        }

        Role userRole = roleRepo.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new AccessDeniedCustomException(ExceptionMessage.ACCESS_DENIED.get()));

        AuthUser user = new AuthUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles().add(userRole);

        userRepository.save(user);

        return generateAuthResponse(user);
    }

    /**
     * Login: transactional so we can safely extract roles from managed entity.
     */
    @Override
    @Transactional
    public AuthResponseDTO login(AuthDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthUser user = (AuthUser) authentication.getPrincipal();

        return generateAuthResponse(user);
    }

    /**
     * Refresh: read-only transaction and load user with roles explicitly to avoid lazy/detached access.
     */
    @Override
    @Transactional
    public Map<String, String> refresh(String refreshToken) {
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException(ExceptionMessage.INVALID_TOKEN.get());
        }

        AuthUser tokenUser = refreshTokenService.getUserFromRefreshToken(refreshToken);

        AuthUser userWithRoles = userRepository.findByUsername(tokenUser.getUsername())
                .orElseThrow(() -> new InvalidTokenException(ExceptionMessage.INVALID_TOKEN.get()));

        refreshTokenService.deleteRefreshTokensForUser(userWithRoles);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userWithRoles);

        Set<String> roleNames = userWithRoles.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        String newAccessToken = jwtUtil.generateToken(userWithRoles.getUsername(), roleNames);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken.getToken()
        );
    }


    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Generates access and refresh tokens for the given user.
     * Called inside @Transactional methods, so user entity is managed and roles are available.
     *
     * @param user the user (managed entity)
     * @return auth response with tokens
     */
    private AuthResponseDTO generateAuthResponse(AuthUser user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getUsername(), roleNames);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDTO(token, user.getUsername(), refreshToken.getToken());
    }
}
