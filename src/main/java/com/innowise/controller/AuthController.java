package com.innowise.controller;

import com.innowise.data.entity.AuthUser;
import com.innowise.data.entity.RefreshToken;
import com.innowise.data.entity.Role;
import com.innowise.data.repository.RoleRepo;
import com.innowise.data.repository.UserRepo;
import com.innowise.service.RefreshTokenService;
import com.innowise.util.JwtUtil;
import com.innowise.util.dto.resp.AuthResponseDTO;
import com.innowise.util.dto.rq.LoginRequestDTO;
import com.innowise.util.dto.rq.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * Registers a new user and returns a JWT access token.
     *
     * @param registerRequest contains username and password
     * @return {@link AuthResponseDTO} with generated access token and username
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        if (userRepo.existsByUsername(registerRequest.username())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponseDTO(null, "Username is already taken", null));
        }

        AuthUser user = new AuthUser();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role userRole = roleRepo.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.getRoles().add(userRole);

        userRepo.save(user);

        String token = jwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), refreshToken.getToken()));
    }


    /**
     * Authenticates the user and returns a JWT access token.
     *
     * @param loginRequest username and password for authentication
     * @return {@link AuthResponseDTO} with access token and username
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthUser user = userRepo.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), refreshToken.getToken()));
    }

    /**
     * Generates a new access token using a valid refresh token.
     *
     * @param refreshToken a valid refresh token string
     * @return a map with new access token and original refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestParam String refreshToken) {
        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            String username = refreshTokenService.getUsernameFromRefreshToken(refreshToken);

            AuthUser user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = jwtUtil.generateToken(user);
            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired refresh token"));
    }
}
