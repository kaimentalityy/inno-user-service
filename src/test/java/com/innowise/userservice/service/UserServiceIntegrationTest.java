package com.innowise.userservice.service;

import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.dao.UserRepository;
import com.innowise.userservice.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    }

    @Test
    void testFindById() {
        User user = createTestUser("Jane", "Smith", "jane@example.com");
        UserDto found = userService.findById(user.getId());

        assertThat(found.name()).isEqualTo("Jane");
    }

    @Test
    void testFindByIds() {
        User u1 = createTestUser("U1", "Last1", "u1@example.com");
        User u2 = createTestUser("U2", "Last2", "u2@example.com");

        List<UserDto> results = userService.findByIds(List.of(u1.getId(), u2.getId()));

        assertThat(results).hasSize(2);
        assertThat(results.stream().map(UserDto::email))
                .contains("u1@example.com", "u2@example.com");
    }

    @Test
    void testFindByEmail() {
        User user = createTestUser("Mike", "Brown", "mike@example.com");
        UserDto found = userService.findByEmail("mike@example.com");

        assertThat(found).isNotNull();
        assertThat(found.email()).isEqualTo("mike@example.com");
    }

    @Test
    void testSearchUsers() {
        createTestUser("Alice", "A", "alice@example.com");
        createTestUser("Bob", "B", "bob@example.com");

        Page<UserDto> page = userService.searchUsers(null, null, null, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSizeGreaterThanOrEqualTo(2);
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
