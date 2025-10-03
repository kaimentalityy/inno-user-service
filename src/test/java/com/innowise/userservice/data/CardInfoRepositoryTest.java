package com.innowise.userservice.data;

import com.innowise.userservice.BaseIntegrationTest;
import com.innowise.userservice.model.entity.CardInfo;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.dao.CardInfoRepository;
import com.innowise.userservice.repository.dao.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CardInfoRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = userRepository.save(createUser("Kiryl", "Savenka", "kiryl.savenka@innowise.com"));

        CardInfo card = new CardInfo();
        card.setUser(user);
        card.setNumber("1234567890123456");
        card.setHolder("Kiryl Savenka");
        card.setExpirationDate(LocalDate.of(2030, 12, 31));

        CardInfo saved = cardInfoRepository.save(card);
        Optional<CardInfo> retrieved = cardInfoRepository.findById(saved.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("1234567890123456", retrieved.get().getNumber());
    }

    @Test
    void testFindByUserId() {
        User user = userRepository.save(createUser("Dima", "Kosoy", "dima@kosoy.com"));

        CardInfo c1 = createCard(user, "1111222233334444");
        CardInfo c2 = createCard(user, "5555666677778888");
        cardInfoRepository.saveAll(List.of(c1, c2));

        List<CardInfo> cards = cardInfoRepository.findByUserId(user.getId());
        assertEquals(2, cards.size());
    }

    @Test
    void testFindCardsByIds() {
        User user = userRepository.save(createUser("Gleb", "Gluhoy", "bob@brown.com"));

        CardInfo c1 = cardInfoRepository.save(createCard(user, "0000111122223333"));
        CardInfo c2 = cardInfoRepository.save(createCard(user, "4444555566667777"));

        List<CardInfo> cards = cardInfoRepository.findCardsByIds(List.of(c1.getId(), c2.getId()));
        assertEquals(2, cards.size());
    }

    @Test
    void testFindByCardNumberNative() {
        User user = userRepository.save(createUser("Ivan", "Petrov", "ivan@petrov.com"));
        cardInfoRepository.save(createCard(user, "9999000011112222"));

        CardInfo found = cardInfoRepository.findByCardNumberNative("9999000011112222");
        assertNotNull(found);
        assertEquals("Ivan Petrov", found.getHolder());
    }

    @Test
    void testCascadeDeleteUser() {
        User user = createUser("Alexander", "Hromoy", "alex@hromoy.com");
        CardInfo card = createCard(user, "9999888877776666");
        user.getCards().add(card);
        userRepository.save(user);

        Long cardId = card.getId();
        userRepository.delete(user);

        userRepository.flush();

        assertFalse(cardInfoRepository.findById(cardId).isPresent());
    }

    private User createUser(String name, String surname, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail(email);
        return user;
    }

    private CardInfo createCard(User user, String number) {
        CardInfo card = new CardInfo();
        card.setUser(user);
        card.setNumber(number);
        card.setHolder(user.getName() + " " + user.getSurname());
        card.setExpirationDate(LocalDate.of(2030, 12, 31));
        return card;
    }
}
