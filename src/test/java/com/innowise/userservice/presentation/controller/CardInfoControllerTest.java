package com.innowise.userservice.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.innowise.userservice.business.service.CardInfoService;
import com.innowise.userservice.presentation.dto.request.CreateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.response.CardInfoDto;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardInfoController.class)
class CardInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardInfoService cardInfoService;

    @Test
    void testCreateCard() throws Exception {
        CreateCardInfoDto createDto = new CreateCardInfoDto(
                1L,
                "1234567890123456",
                "John Doe",
                LocalDate.of(2030, 12, 31)
        );

        CardInfoDto responseDto = new CardInfoDto(
                1L,
                "1234567890123456",
                "John Doe",
                LocalDate.of(2030, 12, 31)
        );

        Mockito.when(cardInfoService.createCardInfo(any(CreateCardInfoDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/cards/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"))
                .andExpect(jsonPath("$.cardHolder").value("John Doe"))
                .andExpect(jsonPath("$.cardExpiryDate").value("2030-12-31"));
    }

    @Test
    void testUpdateCard() throws Exception {
        UpdateCardInfoDto updateDto = new UpdateCardInfoDto(
                1L,
                "9876543210987654",
                "Jane Doe",
                LocalDate.of(2031, 1, 1)
        );

        CardInfoDto updatedDto = new CardInfoDto(
                1L,
                "9876543210987654",
                "Jane Doe",
                LocalDate.of(2031, 1, 1)
        );

        Mockito.when(cardInfoService.updateCardInfo(eq(1L), any(UpdateCardInfoDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("9876543210987654"))
                .andExpect(jsonPath("$.cardHolder").value("Jane Doe"))
                .andExpect(jsonPath("$.cardExpiryDate").value("2031-01-01"));
    }


    @Test
    void testDeleteCard() throws Exception {
        mockMvc.perform(delete("/api/cards/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(cardInfoService).deleteCardInfo(1L);
    }
}
