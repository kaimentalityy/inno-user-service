package com.innowise.userservice.controller;

import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.service.impl.CardInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardInfoControllerTest {

    @Mock
    private CardInfoService cardInfoService;

    @InjectMocks
    private CardInfoController cardInfoController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCard() {
        CardInfoDto dto = new CardInfoDto(null, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        CardInfoDto created = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));

        when(cardInfoService.create(dto)).thenReturn(created);

        ResponseEntity<CardInfoDto> response = cardInfoController.createCard(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(created, response.getBody());
        verify(cardInfoService).create(dto);
    }

    @Test
    void testUpdateCard() {
        CardInfoDto dto = new CardInfoDto(null, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        CardInfoDto updated = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));

        when(cardInfoService.update(1L, dto)).thenReturn(updated);

        ResponseEntity<CardInfoDto> response = cardInfoController.updateCard(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
        verify(cardInfoService).update(1L, dto);
    }

    @Test
    void testDeleteCard() {
        doNothing().when(cardInfoService).delete(1L);

        ResponseEntity<Void> response = cardInfoController.deleteCard(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(cardInfoService).delete(1L);
    }

    @Test
    void testGetCard() {
        CardInfoDto card = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        when(cardInfoService.findById(1L)).thenReturn(card);

        ResponseEntity<CardInfoDto> response = cardInfoController.getCard(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(card, response.getBody());
        verify(cardInfoService).findById(1L);
    }

    @Test
    void testSearchCards_byIds() {
        Pageable pageable = Pageable.unpaged();
        CardInfoDto card = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));

        when(cardInfoService.findByIds(List.of(1L))).thenReturn(List.of(card));

        ResponseEntity<Page<CardInfoDto>> response = cardInfoController.searchCards(List.of(1L), null, null, null, pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(card, response.getBody().getContent().get(0));
        verify(cardInfoService).findByIds(List.of(1L));
    }

    @Test
    void testSearchCards_standardSearch() {
        Pageable pageable = Pageable.unpaged();
        CardInfoDto card = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        Page<CardInfoDto> page = new PageImpl<>(List.of(card));

        when(cardInfoService.searchCards(1L, "1234567890123", "John Doe", pageable)).thenReturn(page);

        ResponseEntity<Page<CardInfoDto>> response = cardInfoController.searchCards(null, 1L, "1234567890123", "John Doe", pageable);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(card, response.getBody().getContent().get(0));
        verify(cardInfoService).searchCards(1L, "1234567890123", "John Doe", pageable);
    }
}
