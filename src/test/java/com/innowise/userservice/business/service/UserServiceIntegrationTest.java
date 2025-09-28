package com.innowise.userservice.business.service;

import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.business.service.impl.UserService;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateUser() {
        UserDto dto = new UserDto(null, "John", "Doe", LocalDate.of(1990, 1, 1), "john@example.com", null);
        UserDto created = userService.create(dto);

        assertThat(created).isNotNull();
        assertThat(created.email()).isEqualTo("john@example.com");
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetUserById() {
        User user = createTestUser("Jane", "Smith", "jane@example.com");
        UserDto found = userService.findById(user.getId());

        assertThat(found.name()).isEqualTo("Jane");
        assertThat(found.email()).isEqualTo("jane@example.com");
    }

    @Test
    void testSearchUsersByEmail() {
        Pageable pageable = PageRequest.of(0, 10);
        createTestUser("Mike", "Brown", "mike@example.com");
        Page<UserDto> found = userService.searchUsers(null, null, "mike@example.com", pageable);

        assertThat(found).hasSize(1);
        assertThat(found.getContent().getFirst().name()).isEqualTo("Mike");
    }

    @Test
    void testSearchUsersByMultiple() {
        Pageable pageable = PageRequest.of(0, 10);
        createTestUser("User1", "Last1", "u1@example.com");
        createTestUser("User2", "Last2", "u2@example.com");

        Page<UserDto> users = userService.searchUsers(null, null, null, pageable);

        assertThat(users.stream().map(UserDto::email))
                .contains("u1@example.com", "u2@example.com");
    }

    @Test
    void testUpdateUser() {
        User user = createTestUser("Old", "Name", "old@example.com");

        UserDto dto = new UserDto(user.getId(), "New", "Name", LocalDate.of(1995, 5, 5), "new@example.com", null);
        UserDto updated = userService.update(user.getId(), dto);

        assertThat(updated.name()).isEqualTo("New");
        assertThat(updated.email()).isEqualTo("new@example.com");
    }

    @Test
    void testDeleteUser() {
        User user = createTestUser("Delete", "Me", "delete@example.com");
        userService.delete(user.getId());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void testDeleteUserWithNullIdThrows() {
        assertThrows(RuntimeException.class, () -> userService.delete(null));
    }

    private User createTestUser(String name, String surname, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        return userRepository.save(user);
    }
}
