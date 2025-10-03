package com.innowise.userservice.controller;

import com.innowise.userservice.service.impl.UserService;
import com.innowise.userservice.model.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            Pageable pageable) {

        if (ids != null && !ids.isEmpty()) {
            List<UserDto> results = userService.findByIds(ids);
            Page<UserDto> page = PageableExecutionUtils.getPage(results, pageable, results::size);
            return ResponseEntity.ok(page);
        }

        if (email != null) {
            UserDto user = userService.findByEmail(email);
            List<UserDto> results = user != null ? List.of(user) : List.of();
            Page<UserDto> page = PageableExecutionUtils.getPage(results, pageable, results::size);
            return ResponseEntity.ok(page);
        }

        return ResponseEntity.ok(userService.searchUsers(name, surname, email, pageable));
    }

}


