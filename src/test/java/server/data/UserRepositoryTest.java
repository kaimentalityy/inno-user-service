package server.data;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import server.data.entity.User;
import server.data.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        User user = new User();
        user.setName("Kiryl");
        user.setSurname("Savenka");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail("kiryl.savenka@innowise.com");

        User savedUser = userRepository.save(user);
        Optional<User> retrieved = userRepository.findById(savedUser.getId());

        assertTrue(retrieved.isPresent());
        assertEquals("Kiryl", retrieved.get().getName());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setName("Alexey");
        user.setSurname("Krivoy");
        user.setBirthDate(LocalDate.of(2010, 5, 10));
        user.setEmail("alexey@krivoy.com");

        userRepository.save(user);
        Optional<User> found = userRepository.findByEmail("alexey@krivoy.com");

        assertTrue(found.isPresent());
        assertEquals("Alexey", found.get().getName());
    }

    @Test
    void testFindUsersByIds() {
        User u1 = userRepository.save(createUser("A", "1", "a@a.com"));
        User u2 = userRepository.save(createUser("B", "2", "b@b.com"));

        List<User> users = userRepository.findUsersByIds(List.of(u1.getId(), u2.getId()));

        assertEquals(2, users.size());
    }

    @Test
    void testDeleteUser() {
        User user = userRepository.save(createUser("C", "3", "c@c.com"));
        Long id = user.getId();
        userRepository.deleteById(id);

        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    void testFindUserByEmailNative() {
        User user = new User();
        user.setName("Ivan");
        user.setSurname("Petrov");
        user.setBirthDate(LocalDate.of(1995, 3, 15));
        user.setEmail("ivan@petrov.com");

        userRepository.save(user);
        Optional<User> found = userRepository.findUserByEmailNative("ivan@petrov.com");

        assertTrue(found.isPresent());
        assertEquals("Ivan", found.get().getName());
    }

    private User createUser(String name, String surname, String email) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setBirthDate(LocalDate.of(2000, 2, 2));
        user.setEmail(email);
        return user;
    }
}

