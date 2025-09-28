package com.innowise.userservice.business.service.impl;

import com.innowise.userservice.business.mapper.CardInfoMapper;
import com.innowise.userservice.business.service.CrudService;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.CardInfoRepository;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.data.specification.CardInfoSpecifications;
import com.innowise.userservice.presentation.dto.CardInfoDto;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Service class for managing {@link CardInfo} entities.
 * <p>
 * Provides CRUD operations and custom queries for card information.
 * Integrates with Redis caching for optimized performance.
 * </p>
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cards")
public class CardInfoService implements CrudService<CardInfoDto, Long> {

    private final CardInfoRepository cardInfoRepository;
    private final CardInfoMapper cardInfoMapper;
    private final UserRepository userRepository;

    /**
     * Creates a new card entry in the database.
     *
     * @param cardInfoDto DTO containing card creation details
     * @return the created card as a {@link CardInfoDto}
     * @throws EntityNotFoundException if the associated user does not exist
     */
    @Override
    @Transactional
    public CardInfoDto create(CardInfoDto cardInfoDto) {
        User user = userRepository.findById(cardInfoDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User", "id", cardInfoDto.userId()));

        CardInfo card = cardInfoMapper.toEntity(cardInfoDto);
        card.setUser(user);

        CardInfo saved = cardInfoRepository.save(card);
        return cardInfoMapper.toDto(saved);
    }

    /**
     * Updates an existing card entry in the database.
     *
     * @param id the ID of the card to update
     * @param cardInfoDto DTO containing updated card details
     * @return the updated card as a {@link CardInfoDto}
     * @throws EntityNotFoundException if the card does not exist
     */
    @Override
    @CachePut(key = "#id")
    @Transactional
    public CardInfoDto update(Long id, CardInfoDto cardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card", "id", id));

        cardInfoMapper.updateEntity(cardInfoDto, cardInfo);
        return cardInfoMapper.toDto(cardInfoRepository.save(cardInfo));
    }

    /**
     * Deletes a card entry by its ID.
     *
     * @param id the ID of the card to delete
     */
    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void delete(Long id) {
        cardInfoRepository.deleteById(id);
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param id the ID of the card
     * @return the card as a {@link CardInfoDto}
     * @throws EntityNotFoundException if the card does not exist
     */
    @Override
    @Cacheable(key = "#id")
    public CardInfoDto findById(Long id) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card", "id", id));
        return cardInfoMapper.toDto(cardInfo);
    }

    public Page<CardInfoDto> searchCards(Long userId, String cardNumber, String cardHolder, Pageable pageable) {
        Specification<CardInfo> spec = Specification.where(null);

        if (userId != null) {
            spec = spec.and(CardInfoSpecifications.hasUserId(userId));
        }
        if (cardNumber != null) {
            spec = spec.and(CardInfoSpecifications.hasCardNumber(cardNumber));
        }
        if (cardHolder != null) {
            spec = spec.and(CardInfoSpecifications.hasCardHolder(cardHolder));
        }

        return cardInfoRepository.findAll(spec, pageable)
                .map(cardInfoMapper::toDto);
    }
}
