package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.model.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    void testToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmail("john@example.com");

        UserDto dto = mapper.toUserDto(user);

        assertEquals("John", dto.name());
        assertEquals("Doe", dto.surname());
        assertEquals(LocalDate.of(1990, 1, 1), dto.birthDate());
        assertEquals("john@example.com", dto.email());
        assertEquals(Collections.emptyList(), dto.cards());
    }

    @Test
    void testToUserEntity() {
        UserDto dto = new UserDto(null, "Alice", "Smith", LocalDate.of(1995, 5, 5),
                "alice@example.com", Collections.emptyList());

        User user = mapper.toUser(dto);

        assertEquals("Alice", user.getName());
        assertEquals("Smith", user.getSurname());
        assertEquals(LocalDate.of(1995, 5, 5), user.getBirthDate());
        assertEquals("alice@example.com", user.getEmail());
        assertNull(user.getId());
        assertTrue(user.getCards().isEmpty());
    }
}
