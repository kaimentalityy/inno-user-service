package com.innowise.userservice.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.innowise.userservice.business.service.UserService;
import com.innowise.userservice.presentation.dto.request.CreateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserWithCardsDto;
import com.innowise.userservice.presentation.dto.response.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user account.
     * Returns the created user DTO with HTTP 201 Created.
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto> createAccount(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = userService.createUser(createUserDto);
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
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates user information without modifying cards.
     * Returns the updated user DTO with HTTP 200 OK.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
        UserDto updatedUser = userService.updateUser(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Updates user information including cards.
     * Returns the updated user DTO with HTTP 200 OK.
     */
    @PatchMapping("/{id}/with-cards")
    public ResponseEntity<UserDto> updateUserWithCards(@PathVariable Long id, @Valid @RequestBody UpdateUserWithCardsDto updateUserDto) {
        UserDto updatedUser = userService.updateUserWithCards(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }
}
