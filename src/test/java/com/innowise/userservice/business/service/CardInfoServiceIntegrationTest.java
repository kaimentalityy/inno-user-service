package com.innowise.userservice.business.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.CardInfoRepository;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.request.CreateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.response.CardInfoDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        CreateCardInfoDto dto = new CreateCardInfoDto(
                user.getId(),
                "1234567890123456",
                "John Doe",
                LocalDate.of(2030, 12, 31)
        );

        CardInfoDto created = cardInfoService.createCardInfo(dto);

        assertThat(created).isNotNull();
        assertThat(created.cardNumber()).isEqualTo("1234567890123456");
        assertThat(created.cardHolder()).isEqualTo("John Doe");
        assertThat(cardInfoRepository.count()).isEqualTo(1);
    }

    @Test
    void testGetCardInfo() {
        CardInfo card = createTestCard("1111222233334444");

        CardInfoDto dto = cardInfoService.getCardInfo(card.getId());

        assertThat(dto.cardNumber()).isEqualTo("1111222233334444");
    }

    @Test
    void testGetCardByNumber() {
        CardInfo card = createTestCard("5555666677778888");

        CardInfoDto dto = cardInfoService.getCardByNumber("5555666677778888");

        assertThat(dto.cardHolder()).isEqualTo(card.getCardHolder());
    }

    @Test
    void testGetCardsByUserId() {
        User user = createTestUser("Alice", "Smith", "alice@example.com");

        createTestCard("1111000011110001", user);
        createTestCard("2222000022220002", user);

        List<CardInfoDto> cards = cardInfoService.getCardsByUserId(user.getId());

        assertThat(cards).hasSize(2);
    }

    @Test
    void testGetCardsByIds() {
        CardInfo card1 = createTestCard("3333000033330003");
        CardInfo card2 = createTestCard("4444000044440004");

        List<CardInfoDto> cards = cardInfoService.getCardsByIds(List.of(card1.getId(), card2.getId()));

        assertThat(cards).hasSize(2);
    }

    @Test
    void testUpdateCardInfo() {
        CardInfo card = createTestCard("9999888877776666");

        UpdateCardInfoDto dto = new UpdateCardInfoDto(
                card.getId(),
                "1111222233334444",
                "Updated Holder",
                LocalDate.of(2035, 1, 1)
        );
        CardInfoDto updated = cardInfoService.updateCardInfo(card.getId(), dto);

        assertThat(updated.cardNumber()).isEqualTo("1111222233334444");
        assertThat(updated.cardHolder()).isEqualTo("Updated Holder");
    }

    @Test
    void testDeleteCardInfo() {
        CardInfo card = createTestCard("5555444433332222");

        cardInfoService.deleteCardInfo(card.getId());

        assertThat(cardInfoRepository.findById(card.getId())).isEmpty();
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
        card.setCardNumber(cardNumber);
        card.setCardHolder(user.getName() + " " + user.getSurname());
        card.setCardExpiryDate(LocalDate.of(2030, 12, 31));
        card.setUser(user);
        return cardInfoRepository.save(card);
    }
}
