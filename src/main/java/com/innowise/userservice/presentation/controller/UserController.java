package com.innowise.userservice.presentation.controller;

import com.innowise.userservice.business.service.impl.UserService;
import com.innowise.userservice.presentation.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user account.
     * Returns the created user DTO with HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<UserDto> createAccount(@Valid @RequestBody UserDto createUserDto) {
        UserDto createdUser = userService.create(createUserDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    /**
     * Deletes a user by ID.
     * Returns HTTP 204 No Content if deletion is successful.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates user information without modifying cards.
     * Returns the updated user DTO with HTTP 200 OK.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateAccount(@PathVariable Long id, @Valid @RequestBody UserDto updateUserDto) {
        UserDto updatedUser = userService.update(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Get a user by ID.
     *
     * @param id the user ID
     * @return the user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Search users with optional filters and pagination.
     *
     * @param name    optional first name filter
     * @param surname optional surname filter
     * @param email   optional email filter
     * @param pageable pagination info
     * @return a page of users matching the filters
     */
    @GetMapping
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        Page<UserDto> list = userService.searchUsers(name, surname, email, pageable);
        return ResponseEntity.ok(list);
    }

}
