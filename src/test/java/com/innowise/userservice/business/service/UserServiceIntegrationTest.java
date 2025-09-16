package com.innowise.userservice.business.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.request.CreateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserWithCardsDto;
import com.innowise.userservice.presentation.dto.response.UserDto;

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
        CreateUserDto dto = new CreateUserDto("John", "Doe",LocalDate.of(1990, 1, 1) ,"john@example.com",null);
        UserDto created = userService.createUser(dto);

        assertThat(created).isNotNull();
        assertThat(created.email()).isEqualTo("john@example.com");
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetUserById() {
        User user = createTestUser("Jane", "Smith", "jane@example.com");
        UserDto found = userService.getUserById(user.getId());

        assertThat(found.name()).isEqualTo("Jane");
        assertThat(found.email()).isEqualTo("jane@example.com");
    }

    @Test
    void testFindByEmail() {
        createTestUser("Mike", "Brown", "mike@example.com");
        UserDto found = userService.findByEmail("mike@example.com");

        assertThat(found.name()).isEqualTo("Mike");
    }

    @Test
    void testFindByEmailNative() {
        createTestUser("Anna", "Taylor", "anna@example.com");
        UserDto found = userService.findByEmailNative("anna@example.com");

        assertThat(found.email()).isEqualTo("anna@example.com");
    }

    @Test
    void testFindUsersByIds() {
        User user1 = createTestUser("User1", "Last1", "u1@example.com");
        User user2 = createTestUser("User2", "Last2", "u2@example.com");

        List<UserDto> users = userService.findUsersByIds(List.of(user1.getId(), user2.getId()));

        assertThat(users).hasSize(2);
        assertThat(users.stream().map(UserDto::email)).contains("u1@example.com", "u2@example.com");
    }

    @Test
    void testUpdateUser() {
        User user = createTestUser("Old", "Name", "old@example.com");

        UpdateUserDto dto = new UpdateUserDto("New", "Name", LocalDate.of(1995, 5, 5), "new@example.com");
        UserDto updated = userService.updateUser(user.getId(), dto);

        assertThat(updated.name()).isEqualTo("New");
        assertThat(updated.email()).isEqualTo("new@example.com");
    }

    @Test
    void testUpdateUserWithCards() {
        User user = createTestUser("Card", "User", "card@example.com");

        UpdateCardInfoDto card1 = new UpdateCardInfoDto(null, "1234", "Card Holder 1", LocalDate.of(2030, 12, 31));
        UpdateCardInfoDto card2 = new UpdateCardInfoDto(null, "5678", "Card Holder 2", LocalDate.of(2031, 11, 30));

        UpdateUserWithCardsDto dto = new UpdateUserWithCardsDto(
                "Card", "User",  LocalDate.of(1990, 1, 1),"card@example.com", List.of(card1, card2)
        );

        UserDto updated = userService.updateUserWithCards(user.getId(), dto);

        assertThat(updated.cards()).hasSize(2);
        assertThat(updated.cards().getFirst().cardNumber()).isEqualTo("1234");
    }

    @Test
    void testDeleteUser() {
        User user = createTestUser("Delete", "Me", "delete@example.com");
        userService.deleteUser(user.getId());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void testDeleteUserWithNullIdThrows() {
        assertThrows(RuntimeException.class, () -> userService.deleteUser(null));
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
