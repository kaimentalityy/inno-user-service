package com.innowise.config;

import com.innowise.data.entity.Role;
import com.innowise.data.repository.RoleRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Initializes default roles in the database.
 * <p>
 * This component runs automatically after the application starts
 * and inserts default roles (ROLE_USER, ROLE_ADMIN) if none exist.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    /** Repository for accessing role data. */
    private final RoleRepo roleRepo;

    /**
     * Populates the roles table with default values if it is empty.
     * This method is executed once after the Spring context is initialized.
     */
    @PostConstruct
    public void init() {
        if (roleRepo.count() == 0) {
            roleRepo.save(new Role(1L, "ROLE_USER"));
            roleRepo.save(new Role(2L, "ROLE_ADMIN"));
        }
    }
}
