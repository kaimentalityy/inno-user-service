package com.innowise.repository;

import com.innowise.model.entity.AuthUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<AuthUser> findByUsername(String username);

    boolean existsByUsername(String username);

}
