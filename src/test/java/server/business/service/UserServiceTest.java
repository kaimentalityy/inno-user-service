package server.business.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.business.mapper.UserMapper;
import server.data.entity.CardInfo;
import server.data.entity.User;
import server.data.repository.UserRepository;
import server.presentation.dto.request.CreateUserDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.request.UpdateUserDto;
import server.presentation.dto.request.UpdateUserWithCardsDto;
import server.presentation.dto.response.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        CreateUserDto dto = new CreateUserDto("John", "Doe", LocalDate.now(), "john@example.com", null);
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");

        when(userMapper.toUser(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null));

        UserDto result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("John", result.name());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser() {
        UpdateUserDto dto = new UpdateUserDto("John Updated", "Doe", LocalDate.now(), "john@example.com");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserDto(existingUser)).thenReturn(new UserDto(1L, "John Updated", "Doe", LocalDate.now(), "john@example.com", null));

        UserDto result = userService.updateUser(1L, dto);

        assertNotNull(result);
        assertEquals("John Updated", result.name());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUserWithCards() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setSurname("Old Surname");
        existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
        existingUser.setEmail("old@example.com");

        CardInfo existingCard = new CardInfo();
        existingCard.setId(10L);
        existingCard.setCardNumber("1234");
        existingCard.setCardHolder("Old Holder");
        existingCard.setCardExpiryDate(LocalDate.of(2030, 1, 1));
        existingCard.setUser(existingUser);
        existingUser.setCards(new ArrayList<>(List.of(existingCard)));

        UpdateCardInfoDto existingCardDto = new UpdateCardInfoDto(10L, "5678", "Updated Holder", LocalDate.of(2031, 1, 1));
        UpdateCardInfoDto newCardDto = new UpdateCardInfoDto(null, "9999", "New Holder", LocalDate.of(2035, 1, 1));

        UpdateUserWithCardsDto dto = new UpdateUserWithCardsDto(
                "New Name",
                "New Surname",
                LocalDate.of(1995, 5, 5),
                "new@example.com",
                List.of(existingCardDto, newCardDto)
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toUserDto(any(User.class)))
                .thenReturn(new UserDto(1L, "New Name", "New Surname", LocalDate.of(1995, 5, 5), "new@example.com", List.of()));

        UserDto result = userService.updateUserWithCards(1L, dto);

        assertNotNull(result);
        assertEquals("New Name", existingUser.getName());
        assertEquals("New Surname", existingUser.getSurname());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals(2, existingUser.getCards().size());
        assertEquals("5678", existingUser.getCards().get(0).getCardNumber());
        assertEquals("9999", existingUser.getCards().get(1).getCardNumber());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).toUserDto(existingUser);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
