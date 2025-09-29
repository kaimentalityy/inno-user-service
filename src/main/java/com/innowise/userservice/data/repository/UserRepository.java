package com.innowise.userservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.innowise.userservice.data.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * <p>
 * Provides CRUD operations, as well as custom methods for querying users
 * by email or by a list of IDs. Supports both JPQL and native SQL queries.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users with IDs in the specified list using JPQL.
     *
     * @param ids a list of user IDs
     * @return a list of users whose IDs match the given list
     */
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findUsersByIds(@Param("ids") List<Long> ids);

    /**
     * Finds a user by their email using a native SQL query.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findUserByEmailNative(@Param("email") String email);
}
