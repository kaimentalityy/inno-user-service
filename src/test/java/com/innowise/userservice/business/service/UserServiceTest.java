package com.innowise.userservice.business.service;

import com.innowise.userservice.business.mapper.UserMapper;
import com.innowise.userservice.business.service.impl.UserService;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.UserDto;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        UserDto dto = new UserDto(null, "John", "Doe", LocalDate.now(), "john@example.com", null);
        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");

        when(userMapper.toUser(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(
                new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null)
        );

        UserDto result = userService.create(dto);

        assertNotNull(result);
        assertEquals("John", result.name());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Old");

        UserDto dto = new UserDto(1L, "John Updated", "Doe", LocalDate.now(), "john@example.com", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(userMapper.toUserDto(existingUser)).thenReturn(dto);

        UserDto result = userService.update(1L, dto);

        assertNotNull(result);
        assertEquals("John Updated", result.name());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setId(1L);
        user.setName("John");

        UserDto userDto = new UserDto(1L, "John", "Doe", LocalDate.now(), "john@example.com", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.name());
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                userService.findById(999L)
        );
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.delete(999L));
    }
}
