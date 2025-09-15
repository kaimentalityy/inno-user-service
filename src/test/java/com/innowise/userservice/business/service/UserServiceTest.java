package com.innowise.userservice.business.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.innowise.userservice.business.mapper.UserMapper;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.request.CreateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserWithCardsDto;
import com.innowise.userservice.presentation.dto.response.UserDto;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("John");

        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.name());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                userService.getUserById(999L)
        );
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findByEmail("john@example.com");

        assertNotNull(result);
        assertEquals("john@example.com", result.email());
    }

    @Test
    void testFindByEmailNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                userService.findByEmail("missing@example.com")
        );
    }

    @Test
    void testFindByEmailNative() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userRepository.findUserByEmailNative("john@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findByEmailNative("john@example.com");

        assertNotNull(result);
        assertEquals("john@example.com", result.email());
    }

    @Test
    void testFindByEmailNativeNotFound() {
        when(userRepository.findUserByEmailNative("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                userService.findByEmailNative("missing@example.com")
        );
    }

    @Test
    void testFindUsersByIds() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        List<User> users = Arrays.asList(user1, user2);

        UserDto dto1 = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);
        UserDto dto2 = new UserDto(2L, "Jane", "Doe", LocalDate.now(), "jane@example.com", null);

        when(userRepository.findUsersByIds(Arrays.asList(1L, 2L))).thenReturn(users);
        when(userMapper.toUserDto(user1)).thenReturn(dto1);
        when(userMapper.toUserDto(user2)).thenReturn(dto2);

        List<UserDto> result = userService.findUsersByIds(Arrays.asList(1L, 2L));

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).name());
        assertEquals("Jane", result.get(1).name());
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
