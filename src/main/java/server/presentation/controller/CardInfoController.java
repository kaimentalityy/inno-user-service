package server.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.business.service.CardInfoService;
import server.presentation.dto.request.CreateCardInfoDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.response.CardInfoDto;

import java.util.List;

/**
 * REST controller for managing card information.
 * <p>
 * Provides endpoints for creating, updating, and deleting card information.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardInfoController {

    private final CardInfoService cardInfoService;

    /**
     * Creates a new card information entry.
     *
     * @param createCardInfoDto DTO containing card details
     * @return ResponseEntity with the created card and HTTP 201 status
     */
    @PostMapping("/create")
    public ResponseEntity<CardInfoDto> createCard(@Valid @RequestBody CreateCardInfoDto createCardInfoDto) {
        CardInfoDto createdCard = cardInfoService.createCardInfo(createCardInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    /**
     * Updates an existing card information entry.
     *
     * @param id               the ID of the card to update
     * @param updateCardInfoDto DTO containing updated card details
     * @return ResponseEntity with the updated card and HTTP 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable Long id, @Valid @RequestBody UpdateCardInfoDto updateCardInfoDto) {
        CardInfoDto updatedCard = cardInfoService.updateCardInfo(id, updateCardInfoDto);
        return ResponseEntity.ok(updatedCard);
    }

    /**
     * Deletes a card information entry by ID.
     *
     * @param id the ID of the card to delete
     * @return ResponseEntity with HTTP 204 status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.deleteCardInfo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a card information entry by ID.
     *
     * @param id the ID of the card to retrieve
     * @return ResponseEntity with the card DTO and HTTP 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCardById(@PathVariable Long id) {
        CardInfoDto cardDto = cardInfoService.getCardInfo(id);
        return ResponseEntity.ok(cardDto);
    }

    /**
     * Retrieves all cards associated with a specific user by user ID.
     *
     * @param userId the ID of the user
     * @return ResponseEntity with a list of card DTOs and HTTP 200 OK
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardInfoDto>> getCardsByUser(@PathVariable Long userId) {
        List<CardInfoDto> cards = cardInfoService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }

    /**
     * Retrieves multiple cards by their IDs.
     *
     * @param ids list of card IDs
     * @return ResponseEntity with a list of card DTOs and HTTP 200 OK
     */
    @PostMapping("/ids")
    public ResponseEntity<List<CardInfoDto>> getCardsByIds(@RequestBody List<Long> ids) {
        List<CardInfoDto> cards = cardInfoService.getCardsByIds(ids);
        return ResponseEntity.ok(cards);
    }

    /**
     * Retrieves a card by its card number.
     *
     * @param cardNumber the card number to search for
     * @return ResponseEntity with the card DTO and HTTP 200 OK
     */
    @GetMapping("/number/{cardNumber}")
    public ResponseEntity<CardInfoDto> getCardByNumber(@PathVariable String cardNumber) {
        CardInfoDto cardDto = cardInfoService.getCardByNumber(cardNumber);
        return ResponseEntity.ok(cardDto);
    }
}
