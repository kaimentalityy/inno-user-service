package com.innowise.userservice.presentation.controller;

import com.innowise.userservice.business.service.impl.CardInfoService;
import com.innowise.userservice.presentation.dto.CardInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

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
    @PostMapping
    public ResponseEntity<CardInfoDto> createCard(@Valid @RequestBody CardInfoDto createCardInfoDto) {
        CardInfoDto createdCard = cardInfoService.create(createCardInfoDto);
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
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardInfoDto updateCardInfoDto) {
        CardInfoDto updatedCard = cardInfoService.update(id, updateCardInfoDto);
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
        cardInfoService.delete(id);
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
        CardInfoDto cardDto = cardInfoService.findById(id);
        return ResponseEntity.ok(cardDto);
    }

    /**
     * Searches cards by optional filters: user ID, card number, and cardholder name.
     *
     * @param userId     optional user ID filter
     * @param cardNumber optional card number filter
     * @param cardHolder optional card holder name filter
     * @param pageable pagination info
     * @return list of matching {@link CardInfoDto} objects
     */
    @GetMapping
    public ResponseEntity<Page<CardInfoDto>> searchCards(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            Pageable pageable) {

        Page<CardInfoDto> cards = cardInfoService.searchCards(userId, cardNumber, cardHolder, pageable);
        return ResponseEntity.ok(cards);
    }

}
