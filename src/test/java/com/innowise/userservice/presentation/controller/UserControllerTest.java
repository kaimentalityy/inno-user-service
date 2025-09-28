package com.innowise.userservice.presentation.controller;

import com.innowise.userservice.business.service.impl.UserService;
import com.innowise.userservice.presentation.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        UserDto createUserDto = new UserDto(null, "John", "Doe", LocalDate.now(), "john@example.com", null);
        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userService.create(createUserDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createAccount(createUserDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());

        verify(userService, times(1)).create(createUserDto);
    }

    @Test
    void testDeleteAccount() {
        ResponseEntity<Void> response = userController.deleteAccount(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void testUpdateAccount() {
        UserDto updateUserDto = new UserDto(null, "John Updated", "Doe", LocalDate.now(), "john@example.com", null);
        UserDto updatedUser = new UserDto(1L, "John Updated", "Doe", LocalDate.now(), "john@example.com", null);

        when(userService.update(1L, updateUserDto)).thenReturn(updatedUser);

        ResponseEntity<UserDto> response = userController.updateAccount(1L, updateUserDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());

        verify(userService, times(1)).update(1L, updateUserDto);
    }

    @Test
    void testFindById() {
        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userService.findById(1L)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());

        verify(userService, times(1)).findById(1L);
    }

    @Test
    void testSearchUsers() {

        Pageable pageable = PageRequest.of(0, 10);
        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);
        Page<UserDto> userPage = new PageImpl<>(List.of(userDto), pageable, 1);

        when(userService.searchUsers("John", "Doe", "john@example.com", pageable))
                .thenReturn(userPage);

        ResponseEntity<Page<UserDto>> response = userController.searchUsers("John", "Doe", "john@example.com", pageable);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(userDto, response.getBody().getContent().getFirst());

        verify(userService, times(1)).searchUsers("John", "Doe", "john@example.com", pageable);
    }
}
