package server.business.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import server.business.mapper.CardInfoMapper;
import server.data.entity.CardInfo;
import server.data.repository.CardInfoRepository;
import server.presentation.dto.request.CreateCardInfoDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.response.CardInfoDto;
import server.util.exceptions.notfound.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link CardInfo} entities.
 * <p>
 * Provides operations for creating, updating, deleting, and fetching card information.
 * Integrates with Redis caching for better performance.
 * </p>
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cards")
public class CardInfoService {

    private final CardInfoRepository cardInfoRepository;
    private final CardInfoMapper cardInfoMapper;

    /**
     * Creates a new card information entry.
     *
     * @param createCardInfoDto DTO containing details for the new card
     * @return a DTO representing the created card
     */
    public CardInfoDto createCardInfo(CreateCardInfoDto createCardInfoDto) {
        CardInfo cardInfo = cardInfoMapper.toEntity(createCardInfoDto);
        cardInfo = save(cardInfo);
        return cardInfoMapper.toDto(cardInfo);
    }

    /**
     * Updates an existing card information entry.
     * <p>
     * Updates the Redis cache with the latest card data.
     * </p>
     *
     * @param id               the ID of the card to update
     * @param updateCardInfoDto DTO containing updated card details
     * @return a {@link CardInfoDto} representing the updated card
     * @throws EntityNotFoundException if the card with the given ID is not found
     */
    @CachePut(key = "#id")
    @Transactional
    public CardInfoDto updateCardInfo(Long id, UpdateCardInfoDto updateCardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card","id", id));

        cardInfoMapper.updateEntity(updateCardInfoDto, cardInfo);

        CardInfo savedCardInfo = cardInfoRepository.save(cardInfo);
        return cardInfoMapper.toDto(savedCardInfo);
    }

    /**
     * Deletes a card information entry by its ID.
     * <p>
     * Evicts the corresponding entry from the Redis cache.
     * </p>
     *
     * @param id the ID of the card to delete
     */
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteCardInfo(Long id) {
        cardInfoRepository.deleteById(id);
    }

    /**
     * Retrieves a card information entry by its ID.
     * <p>
     * Uses Redis caching to improve retrieval performance.
     * </p>
     *
     * @param id the ID of the card to fetch
     * @return a {@link CardInfoDto} representing the fetched card
     * @throws EntityNotFoundException if the card with the given ID is not found
     */
    @Cacheable(key = "#id")
    public CardInfoDto getCardInfo(Long id) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card","id", id));
        return cardInfoMapper.toDto(cardInfo);
    }

    /**
     * Retrieves all cards associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of {@link CardInfoDto} objects
     */
    @Cacheable(key = "#userId")
    public List<CardInfoDto> getCardsByUserId(Long userId) {
        List<CardInfo> cards = cardInfoRepository.findByUserId(userId);
        return cards.stream().map(cardInfoMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves multiple cards by their IDs.
     *
     * @param ids a list of card IDs
     * @return a list of {@link CardInfoDto} objects
     */
    @Cacheable(key = "#ids")
    public List<CardInfoDto> getCardsByIds(List<Long> ids) {
        List<CardInfo> cards = cardInfoRepository.findCardsByIds(ids);
        return cards.stream().map(cardInfoMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a card by its number using native query.
     *
     * @param cardNumber the card number
     * @return a {@link CardInfoDto} representing the card
     * @throws EntityNotFoundException if the card is not found
     */
    @Cacheable(key = "#cardNumber")
    public CardInfoDto getCardByNumber(String cardNumber) {
        CardInfo cardInfo = cardInfoRepository.findByCardNumberNative(cardNumber);
        if (cardInfo == null) {
            throw new EntityNotFoundException("Card","card number", cardNumber);
        }
        return cardInfoMapper.toDto(cardInfo);
    }

    /**
     * Saves a card information entity to the database.
     *
     * @param cardInfo the card entity to save
     * @return the saved card entity
     */
    public CardInfo save(CardInfo cardInfo) {
        return cardInfoRepository.save(cardInfo);
    }
}
