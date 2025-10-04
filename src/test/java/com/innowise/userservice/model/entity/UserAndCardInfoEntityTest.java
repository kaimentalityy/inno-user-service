package com.innowise.userservice.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserAndCardInfoEntityTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setEmail("john@example.com");

        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testCardInfoGettersAndSetters() {
        CardInfo card = new CardInfo();
        User user = new User();
        card.setUser(user);
        card.setNumber("1234567890123");
        card.setHolder("John Doe");
        card.setExpirationDate(LocalDate.of(2030, 12, 31));

        assertEquals(user, card.getUser());
        assertEquals("1234567890123", card.getNumber());
        assertEquals("John Doe", card.getHolder());
        assertEquals(LocalDate.of(2030, 12, 31), card.getExpirationDate());
    }
}
