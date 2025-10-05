package com.innowise.userservice.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testUserFieldsAndAccessors() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(1990, 5, 20));
        user.setEmail("john.doe@example.com");

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getSurname()).isEqualTo("Doe");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 20));
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testCardsListOperations() {
        User user = new User();
        CardInfo card1 = new CardInfo();
        card1.setNumber("1111222233334444");
        card1.setHolder("John Doe");
        card1.setExpirationDate(LocalDate.of(2030, 12, 31));
        card1.setUser(user);

        CardInfo card2 = new CardInfo();
        card2.setNumber("5555666677778888");
        card2.setHolder("John Doe");
        card2.setExpirationDate(LocalDate.of(2032, 12, 31));
        card2.setUser(user);

        // Add cards
        user.getCards().add(card1);
        user.getCards().add(card2);

        assertThat(user.getCards()).hasSize(2);
        assertThat(user.getCards()).containsExactly(card1, card2);

        // Remove a card
        user.getCards().remove(card1);
        assertThat(user.getCards()).hasSize(1);
        assertThat(user.getCards()).contains(card2);
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("email@example.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("email@example.com");

        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("other@example.com");

        // Equals
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        // HashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(1990, 5, 20));
        user.setEmail("john.doe@example.com");

        String str = user.toString();
        assertThat(str).contains("1");
        assertThat(str).contains("John");
        assertThat(str).contains("Doe");
        assertThat(str).contains("1990-05-20");
        assertThat(str).contains("john.doe@example.com");
    }
}
