package com.innowise.orderservice.client;

import com.innowise.orderservice.model.dto.UserInfoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(@Value("${user.service.url}") String userServiceUrl,
                             WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(userServiceUrl).build();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
    public UserInfoDto getUserById(Long userId) {
        return webClient
                .get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserInfoDto.class)
                .block();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
    public UserInfoDto getUserByEmail(String email) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/users/search")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoDto.class)
                .block();
    }

    /**
     * Generic fallback method used for both getUserById and getUserByEmail.
     */
    private UserInfoDto userFallback(Object key, Throwable throwable) {
        log.warn("Circuit breaker triggered for UserService call with key [{}]: {}", key, throwable.getMessage());

        if (key instanceof Long userId) {
            return createFallbackDto(userId, "unknown@example.com");
        } else if (key instanceof String email) {
            return createFallbackDto(-1L, email);
        } else {
            return createFallbackDto(-1L, "unknown@example.com");
        }
    }

    private UserInfoDto createFallbackDto(Long id, String email) {
        return new UserInfoDto(
                id,
                "Unknown",
                "User",
                email
        );
    }
}
