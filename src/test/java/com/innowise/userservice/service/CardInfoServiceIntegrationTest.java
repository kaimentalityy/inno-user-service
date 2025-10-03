package com.innowise.userservice.service;

import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.model.entity.CardInfo;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.dao.CardInfoRepository;
import com.innowise.userservice.repository.dao.UserRepository;
import com.innowise.userservice.service.impl.CardInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class CardInfoServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CardInfoService cardInfoService;

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateCardInfo() {
        User user = createTestUser("John", "Doe", "john@example.com");

        CardInfoDto dto = new CardInfoDto(
                null,
                user.getId(),
                "1234567890123456",
                "John Doe",
                LocalDate.of(2030, 12, 31)
        );

        CardInfoDto created = cardInfoService.create(dto);

        assertThat(created).isNotNull();
        assertThat(created.number()).isEqualTo("1234567890123456");
        assertThat(created.holder()).isEqualTo("John Doe");
    }

    @Test
    void testFindById() {
        CardInfo card = createTestCard("1111222233334444");
        CardInfoDto dto = cardInfoService.findById(card.getId());

        assertThat(dto.number()).isEqualTo("1111222233334444");
        assertThat(dto.holder()).isEqualTo(card.getHolder());
    }

    @Test
    void testFindByIds() {
        CardInfo card1 = createTestCard("1111");
        CardInfo card2 = createTestCard("2222");

        List<CardInfoDto> results = cardInfoService.findByIds(List.of(card1.getId(), card2.getId()));

        assertThat(results).hasSize(2);
    }

    @Test
    void testSearchCards() {
        User user = createTestUser("Alice", "Smith", "alice@example.com");
        createTestCard("1111000011110001", user);
        createTestCard("2222000022220002", user);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CardInfoDto> cards = cardInfoService.searchCards(user.getId(), null, null, pageable);

        assertThat(cards.getContent()).hasSize(2);
    }

    @Test
    void testUpdateCardInfo() {
        CardInfo card = createTestCard("9999888877776666");

        CardInfoDto dto = new CardInfoDto(
                card.getId(),
                card.getUser().getId(),
                "1111222233334444",
                "Updated Holder",
                LocalDate.of(2035, 1, 1)
        );

        CardInfoDto updated = cardInfoService.update(card.getId(), dto);

        assertThat(updated.number()).isEqualTo("1111222233334444");
        assertThat(updated.holder()).isEqualTo("Updated Holder");
    }

    @Test
    void testDeleteCardInfo() {
        CardInfo card = createTestCard("5555444433332222");
        cardInfoService.delete(card.getId());

        assertThat(cardInfoRepository.findById(card.getId())).isEmpty();
    }

    @Test
    void testDeleteCardInfoWithNonExistingIdThrows() {
        assertThrows(RuntimeException.class, () -> cardInfoService.delete(-1L));
    }

    private User createTestUser(String name, String surname, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        return userRepository.save(user);
    }

    private CardInfo createTestCard(String cardNumber) {
        User user = createTestUser("John", "Doe", "user" + System.currentTimeMillis() + "@example.com");
        return createTestCard(cardNumber, user);
    }

    private CardInfo createTestCard(String cardNumber, User user) {
        CardInfo card = new CardInfo();
        card.setNumber(cardNumber);
        card.setHolder(user.getName() + " " + user.getSurname());
        card.setExpirationDate(LocalDate.of(2030, 12, 31));
        card.setUser(user);
        return cardInfoRepository.save(card);
    }
}
