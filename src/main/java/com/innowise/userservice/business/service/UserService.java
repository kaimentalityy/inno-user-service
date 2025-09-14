package com.innowise.userservice.business.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.innowise.userservice.business.mapper.UserMapper;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.request.CreateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserWithCardsDto;
import com.innowise.userservice.presentation.dto.response.UserDto;
import com.innowise.userservice.util.exceptions.badrequest.CustomConstraintViolationException;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link User} entities.
 * <p>
 * Provides operations for creating, updating, and deleting users,
 * including handling of associated card information.
 * </p>
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user with optional associated card information.
     *
     * @param dto DTO containing user details and optional card information
     * @return a DTO representing the created user
     */
    public UserDto createUser(CreateUserDto dto) {
        User user = userMapper.toUser(dto);

        if (user.getCards() != null) {
            user.getCards().forEach(card -> card.setUser(user));
        }

        User saved = save(user);
        return userMapper.toUserDto(saved);
    }

    /**
     * Updates an existing user without modifying their cards.
     *
     * @param id  the ID of the user to update
     * @param dto DTO containing updated user details
     * @return a DTO representing the updated user
     * @throws RuntimeException if the user with the given ID is not found
     */
    @CachePut(key = "#id")
    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User","id", id));
        userMapper.updateUserFromDto(dto, existing);

        User saved = userRepository.save(existing);

        return userMapper.toUserDto(saved);
    }


    /**
     * Updates a user and their associated cards.
     * <p>
     * Cards not present in the request are removed,
     * new cards are added, and existing cards are updated.
     * </p>
     *
     * @param id  the ID of the user to update
     * @param dto DTO containing updated user and card details
     * @return a DTO representing the updated user with cards
     * @throws RuntimeException if the user with the given ID is not found
     */
    @CachePut(key = "#id")
    @Transactional
    public UserDto updateUserWithCards(Long id, UpdateUserWithCardsDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User","id", id));

        user.setName(dto.name());
        user.setSurname(dto.surname());
        user.setBirthDate(dto.birthDate());
        user.setEmail(dto.email());

        user.getCards().removeIf(c -> dto.cards().stream().noneMatch(d -> d.id() != null && d.id().equals(c.getId())));

        for (UpdateCardInfoDto cardDto : dto.cards()) {
            if (cardDto.id() == null) {
                CardInfo newCard = new CardInfo();
                newCard.setUser(user);
                newCard.setCardNumber(cardDto.cardNumber());
                newCard.setCardHolder(cardDto.cardHolder());
                newCard.setCardExpiryDate(cardDto.cardExpiryDate());
                user.getCards().add(newCard);
            } else {
                user.getCards().stream()
                        .filter(c -> c.getId().equals(cardDto.id()))
                        .findFirst()
                        .ifPresent(c -> {
                            c.setCardNumber(cardDto.cardNumber());
                            c.setCardHolder(cardDto.cardHolder());
                            c.setCardExpiryDate(cardDto.cardExpiryDate());
                        });
            }
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    /**
     * Finds a user by their id
     *
     * @param id the ID of the user to search for
     * @return a DTO representing the user
     * @throws EntityNotFoundException if no user with the given id is found
     */

    @Cacheable(key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User","id", id));
        return userMapper.toUserDto(user);
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email to search for
     * @return a DTO representing the user
     * @throws EntityNotFoundException if no user with the given email is found
     */
    @Cacheable(key = "#email")
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
        return userMapper.toUserDto(user);
    }

    /**
     * Finds a user by their email using native SQL.
     *
     * @param email the email to search for
     * @return a DTO representing the user
     * @throws EntityNotFoundException if no user with the given email is found
     */
    @Cacheable(key = "#email")
    public UserDto findByEmailNative(String email) {
        User user = userRepository.findUserByEmailNative(email)
                .orElseThrow(() -> new EntityNotFoundException("User","email", email));
        return userMapper.toUserDto(user);
    }

    /**
     * Finds users by a list of IDs.
     *
     * @param ids a list of user IDs
     * @return a list of DTOs representing the users
     */
    @CacheEvict(key = "#ids")
    public List<UserDto> findUsersByIds(List<Long> ids) {
        List<User> users = userRepository.findUsersByIds(ids);
        return users.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }


    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @throws IllegalArgumentException if the provided ID is null
     */
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) {
            throw new CustomConstraintViolationException("id cannot be null");
        }
        userRepository.deleteById(id);
    }

    /**
     * Saves a user entity to the database.
     *
     * @param user the user entity to save
     * @return the saved user entity
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}
