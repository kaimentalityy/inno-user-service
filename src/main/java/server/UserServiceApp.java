package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the User Service Spring Boot application.
 * <p>
 * This class bootstraps the Spring context and starts the embedded server.
 * </p>
 */
@SpringBootApplication
public class UserServiceApp {

    /**
     * Application entry point.
     *
     * @param args command-line arguments (optional)
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApp.class, args);
    }
}
