package server.business.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.business.mapper.UserMapper;
import server.data.entity.CardInfo;
import server.data.entity.User;
import server.data.repository.UserRepository;
import server.presentation.dto.request.CreateUserDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.request.UpdateUserDto;
import server.presentation.dto.request.UpdateUserWithCardsDto;
import server.presentation.dto.response.UserDto;

/**
 * Service class for managing {@link User} entities.
 * <p>
 * Provides operations for creating, updating, and deleting users,
 * including handling of associated card information.
 * </p>
 */
@Service
@RequiredArgsConstructor
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
    @Transactional
    public UserDto updateUser(Long id, UpdateUserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    @Transactional
    public UserDto updateUserWithCards(Long id, UpdateUserWithCardsDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     * @throws IllegalArgumentException if the provided ID is null
     */
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
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
