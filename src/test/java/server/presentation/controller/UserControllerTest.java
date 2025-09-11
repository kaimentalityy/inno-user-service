package server.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.business.service.UserService;
import server.presentation.dto.request.CreateUserDto;
import server.presentation.dto.request.UpdateUserDto;
import server.presentation.dto.request.UpdateUserWithCardsDto;
import server.presentation.dto.response.UserDto;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        CreateUserDto createUserDto = new CreateUserDto("John", "Doe", LocalDate.now(), "john@example.com", null);
        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userService.createUser(createUserDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createAccount(createUserDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());

        verify(userService, times(1)).createUser(createUserDto);
    }

    @Test
    void testDeleteAccount() {
        ResponseEntity<Void> response = userController.deleteAccount(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testUpdateAccount() {
        UpdateUserDto updateUserDto = new UpdateUserDto("John Updated", "Doe", LocalDate.now(), "john@example.com");
        UserDto updatedUser = new UserDto(1L, "John Updated", "Doe", LocalDate.now(), "john@example.com", null);

        when(userService.updateUser(1L, updateUserDto)).thenReturn(updatedUser);

        ResponseEntity<UserDto> response = userController.updateAccount(1L, updateUserDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());

        verify(userService, times(1)).updateUser(1L, updateUserDto);
    }

    @Test
    void testUpdateUserWithCards() {
        UpdateUserWithCardsDto updateUserWithCardsDto = new UpdateUserWithCardsDto(
                "John Updated",
                "Doe",
                LocalDate.now(),
                "john@example.com",
                List.of()
        );
        UserDto updatedUser = new UserDto(1L, "John Updated", "Doe", LocalDate.now(), "john@example.com", List.of());

        when(userService.updateUserWithCards(1L, updateUserWithCardsDto)).thenReturn(updatedUser);

        ResponseEntity<UserDto> response = userController.updateUserWithCards(1L, updateUserWithCardsDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());

        verify(userService, times(1)).updateUserWithCards(1L, updateUserWithCardsDto);
    }
}
