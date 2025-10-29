package com.innowise.userservice.controller;

import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.service.impl.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardInfoController {

    private final CardInfoService cardInfoService;

    /**
     * Accessible only by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardInfoDto> createCard(@Valid @RequestBody CardInfoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardInfoService.create(dto));
    }

    /**
     * Accessible only by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CardInfoDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardInfoDto dto) {
        return ResponseEntity.ok(cardInfoService.update(id, dto));
    }

    /**
     * Accessible only by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Accessible by ADMIN or USER
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CardInfoDto> getCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.findById(id));
    }

    /**
     * Accessible only by ADMIN
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<CardInfoDto>> searchCards(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            Pageable pageable) {

        if (ids != null && !ids.isEmpty()) {
            List<CardInfoDto> results = cardInfoService.findByIds(ids);
            Page<CardInfoDto> page = PageableExecutionUtils.getPage(results, pageable, results::size);
            return ResponseEntity.ok(page);
        }

        return ResponseEntity.ok(cardInfoService.searchCards(userId, cardNumber, cardHolder, pageable));
    }
}
