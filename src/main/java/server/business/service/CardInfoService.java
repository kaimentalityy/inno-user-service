package server.business.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.business.mapper.CardInfoMapper;
import server.data.entity.CardInfo;
import server.data.repository.CardInfoRepository;
import server.presentation.dto.request.CreateCardInfoDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.response.CardInfoDto;

/**
 * Service class for managing {@link CardInfo} entities.
 * <p>
 * Provides operations for creating, updating, and deleting card information
 * and maps entities to DTOs for API responses.
 * </p>
 */
@Service
@RequiredArgsConstructor
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
     *
     * @param id the ID of the card to update
     * @param updateCardInfoDto DTO containing updated card details
     * @return a DTO representing the updated card
     * @throws RuntimeException if the card with the given ID is not found
     */
    @Transactional
    public CardInfoDto updateCardInfo(Long id, UpdateCardInfoDto updateCardInfoDto) {
        CardInfo cardInfo = cardInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card info not found"));

        cardInfoMapper.updateEntity(updateCardInfoDto, cardInfo);

        CardInfo savedCardInfo = cardInfoRepository.save(cardInfo);
        return cardInfoMapper.toDto(savedCardInfo);
    }

    /**
     * Deletes a card information entry by ID.
     *
     * @param id the ID of the card to delete
     */
    @Transactional
    public void deleteCardInfo(Long id) {
        cardInfoRepository.deleteById(id);
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
