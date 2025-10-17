package com.innowise.orderservice.client;

import com.innowise.orderservice.model.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @Value("${user.service.url}")
    private String userServiceUrl;

    private WebClient getClient() {
        if (webClient == null) {
            webClient = webClientBuilder.baseUrl(userServiceUrl).build();
        }
        return webClient;
    }

    public UserInfoDto getUserById(Long userId) {
        return getClient()
                .get()
                .uri("/api/v1/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserInfoDto.class)
                .block();
    }

    public UserInfoDto getUserByEmail(String email) {
        return getClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/users/search")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoDto.class)
                .block();
    }
}
