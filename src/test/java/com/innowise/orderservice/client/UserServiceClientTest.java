package com.innowise.orderservice.client;

import com.innowise.orderservice.model.dto.UserInfoDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.reflect.Method;

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
        String baseUrl = mockWebServer.url("/").toString();
        userServiceClient = new UserServiceClient(baseUrl, builder);
    }

    @Test
    void getUserById_ShouldReturnUserInfo() throws Exception {
        String body = """
                {"id":1,"email":"test@example.com","name":"John","surname":"Doe"}
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        UserInfoDto dto = userServiceClient.getUserById(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("test@example.com", dto.email());
        assertEquals("John", dto.name());
        assertEquals("Doe", dto.surname());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("/api/users/1", recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void getUserByEmail_ShouldReturnUserInfo() throws Exception {
        String body = """
                {"id":2,"email":"another@example.com","name":"Alice","surname":"Smith"}
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        UserInfoDto dto = userServiceClient.getUserByEmail("another@example.com");

        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals("another@example.com", dto.email());
        assertEquals("Alice", dto.name());
        assertEquals("Smith", dto.surname());

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertTrue(recordedRequest.getPath().contains("/api/users/search?email=another@example.com"));
        assertEquals("GET", recordedRequest.getMethod());
    }

    @Test
    void userFallback_ShouldHandleLongKey() throws Exception {
        Method method = UserServiceClient.class.getDeclaredMethod("userFallback", Object.class, Throwable.class);
        method.setAccessible(true);

        UserInfoDto dto = (UserInfoDto) method.invoke(userServiceClient, 123L, new RuntimeException("Simulated"));
        assertEquals(123L, dto.id());
        assertEquals("unknown@example.com", dto.email());
        assertEquals("Unknown", dto.name());
        assertEquals("User", dto.surname());
    }

    @Test
    void userFallback_ShouldHandleStringKey() throws Exception {
        Method method = UserServiceClient.class.getDeclaredMethod("userFallback", Object.class, Throwable.class);
        method.setAccessible(true);

        UserInfoDto dto = (UserInfoDto) method.invoke(userServiceClient, "test@example.com", new RuntimeException("Simulated"));
        assertEquals(-1L, dto.id());
        assertEquals("test@example.com", dto.email());
    }

    @Test
    void userFallback_ShouldHandleUnknownType() throws Exception {
        Method method = UserServiceClient.class.getDeclaredMethod("userFallback", Object.class, Throwable.class);
        method.setAccessible(true);

        UserInfoDto dto = (UserInfoDto) method.invoke(userServiceClient, new Object(), new RuntimeException("Simulated"));
        assertEquals(-1L, dto.id());
        assertEquals("unknown@example.com", dto.email());
    }

    @Test
    void createFallbackDto_ShouldReturnCorrectUser() throws Exception {
        Method method = UserServiceClient.class.getDeclaredMethod("createFallbackDto", Long.class, String.class);
        method.setAccessible(true);

        UserInfoDto dto = (UserInfoDto) method.invoke(userServiceClient, 999L, "fail@example.com");
        assertEquals(999L, dto.id());
        assertEquals("fail@example.com", dto.email());
        assertEquals("Unknown", dto.name());
        assertEquals("User", dto.surname());
    }
}
