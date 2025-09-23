package com.innowise.controller;

import com.innowise.data.entity.AuthUser;
import com.innowise.data.entity.RefreshToken;
import com.innowise.data.repository.UserRepo;
import com.innowise.service.RefreshTokenService;
import com.innowise.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public String register(@RequestBody AuthUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User registered!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (auth.isAuthenticated()) {
            String accessToken = jwtUtil.generateToken(username);  // use JwtUtil
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);

            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken.getToken()
            );
        }

        throw new RuntimeException("Invalid credentials");
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestParam String token) {
        return jwtUtil.validateToken(token);  // use JwtUtil
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestParam String refreshToken) {
        if (refreshTokenService.validateRefreshToken(refreshToken)) {
            String username = refreshTokenService.getUsernameFromRefreshToken(refreshToken);
            String newAccessToken = jwtUtil.generateToken(username);  // use JwtUtil

            return Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken // same refresh token until it expires
            );
        }
        throw new RuntimeException("Invalid or expired refresh token!");
    }
}
