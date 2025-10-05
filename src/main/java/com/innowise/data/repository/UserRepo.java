package com.innowise.data.repository;

import com.innowise.data.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);
}
