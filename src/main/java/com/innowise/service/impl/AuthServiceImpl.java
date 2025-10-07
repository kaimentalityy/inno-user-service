package com.innowise.service.impl;

import com.innowise.exception.AccessDeniedCustomException;
import com.innowise.exception.InvalidTokenException;
import com.innowise.repository.RoleRepo;
import com.innowise.repository.UserRepo;
import com.innowise.exception.EntityAlreadyExistsException;
import com.innowise.model.dto.resp.AuthResponseDTO;
import com.innowise.model.dto.rq.AuthDto;
import com.innowise.model.entity.AuthUser;
import com.innowise.model.entity.RefreshToken;
import com.innowise.model.entity.Role;
import com.innowise.service.AuthService;
import com.innowise.service.RefreshTokenService;
import com.innowise.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponseDTO register(AuthDto request) {
        if (userRepo.existsByUsername(request.username())) {
            throw new EntityAlreadyExistsException(
                    "User with username '%s' already exists".formatted(request.username())
            );
        }

        Role userRole = roleRepo.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new AccessDeniedCustomException("Default role 'ROLE_USER' not found"));

        AuthUser user = new AuthUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles().add(userRole);

        userRepo.save(user);

        return generateAuthResponse(user);
    }

    @Override
    public AuthResponseDTO login(AuthDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthUser user = (AuthUser) authentication.getPrincipal();

        return generateAuthResponse(user);
    }

    @Override
    public Map<String, String> refresh(String refreshToken) {
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }
        AuthUser user = refreshTokenService.getUserFromRefreshToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(user);
        return Map.of("accessToken", newAccessToken, "refreshToken", refreshToken);
    }

    private AuthResponseDTO generateAuthResponse(AuthUser user) {
        String token = jwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponseDTO(token, user.getUsername(), refreshToken.getToken());
    }
}
