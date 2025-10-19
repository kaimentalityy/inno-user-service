package com.innowise.orderservice.client;

import com.innowise.orderservice.model.dto.UserInfoDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceClientTest {

    private static MockWebServer mockWebServer;
    private UserServiceClient userServiceClient;

    @BeforeAll
    static void startServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        WebClient.Builder builder = WebClient.builder();
        userServiceClient = new UserServiceClient(builder);
        ReflectionTestUtils.setField(userServiceClient, "userServiceUrl", mockWebServer.url("/").toString());
    }

    @Test
    void getUserById_ShouldReturnUserInfo() throws InterruptedException {
        String body = """
                {"id":1,"email":"test@example.com","name":"John Doe"}
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        UserInfoDto dto = userServiceClient.getUserById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("test@example.com", dto.email());
        assertEquals("John Doe", dto.name());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/api/users/1", recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void getUserByEmail_ShouldReturnUserInfo() throws InterruptedException {
        String body = """
                {"id":2,"email":"another@example.com","name":"Alice"}
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        UserInfoDto dto = userServiceClient.getUserByEmail("another@example.com");

        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals("another@example.com", dto.email());
        assertEquals("Alice", dto.name());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertTrue(recordedRequest.getPath().contains("/api/users/search?email=another@example.com"));
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void getUserById_ShouldReuseWebClientInstance() throws Exception {
        // Prepare dummy responses for both calls
        mockWebServer.enqueue(new MockResponse().setBody("{}").addHeader("Content-Type", "application/json"));
        mockWebServer.enqueue(new MockResponse().setBody("{}").addHeader("Content-Type", "application/json"));

        // Before first call: webClient should be null
        Object before = ReflectionTestUtils.getField(userServiceClient, "webClient");
        assertNull(before, "WebClient should be null before first use");

        userServiceClient.getUserById(1L);

        // After first call: webClient should be initialized
        WebClient firstClient = (WebClient) ReflectionTestUtils.getField(userServiceClient, "webClient");
        assertNotNull(firstClient, "WebClient should be initialized after first call");

        userServiceClient.getUserById(2L);

        // After second call: should still be the same instance
        WebClient secondClient = (WebClient) ReflectionTestUtils.getField(userServiceClient, "webClient");
        assertSame(firstClient, secondClient, "WebClient should be reused after initialization");
    }
}
