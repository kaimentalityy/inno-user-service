package com.innowise.userservice.business.service;

import com.innowise.userservice.business.mapper.CardInfoMapper;
import com.innowise.userservice.business.service.impl.CardInfoService;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.CardInfoRepository;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.presentation.dto.CardInfoDto;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInfoMapper cardInfoMapper;

    @InjectMocks
    private CardInfoService cardInfoService;

    public CardInfoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCardInfo() {
        User user = new User();
        user.setId(1L);

        CardInfoDto dto = new CardInfoDto(null, 1L, "1234", "John Doe", LocalDate.now());
        CardInfo entity = new CardInfo();
        CardInfo savedEntity = new CardInfo();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardInfoMapper.toEntity(dto)).thenReturn(entity);
        when(cardInfoRepository.save(entity)).thenReturn(savedEntity);
        when(cardInfoMapper.toDto(savedEntity)).thenReturn(dto);

        CardInfoDto result = cardInfoService.create(dto);

        assertNotNull(result);
        assertEquals("1234", result.cardNumber());
        verify(cardInfoRepository).save(entity);
    }

    @Test
    void testCreateCardInfoUserNotFound() {
        CardInfoDto dto = new CardInfoDto(null, 999L, "1234", "John Doe", LocalDate.now());
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.create(dto));
    }

    @Test
    void testUpdateCardInfo() {
        CardInfo entity = new CardInfo();
        CardInfoDto dto = new CardInfoDto(1L, 1L, "5678", "Jane Doe", LocalDate.now());

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(cardInfoRepository.save(entity)).thenReturn(entity);
        when(cardInfoMapper.toDto(entity)).thenReturn(dto);

        CardInfoDto result = cardInfoService.update(1L, dto);

        assertNotNull(result);
        assertEquals("5678", result.cardNumber());
        verify(cardInfoRepository).save(entity);
    }

    @Test
    void testUpdateCardInfoNotFound() {
        CardInfoDto dto = new CardInfoDto(999L, 1L, "5678", "Jane Doe", LocalDate.now());
        when(cardInfoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.update(999L, dto));
    }

    @Test
    void testFindById() {
        CardInfo entity = new CardInfo();
        entity.setId(1L);
        CardInfoDto dto = new CardInfoDto(1L, 1L, "1234", "John Doe", LocalDate.now());

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(cardInfoMapper.toDto(entity)).thenReturn(dto);

        CardInfoDto result = cardInfoService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void testFindByIdNotFound() {
        when(cardInfoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.findById(999L));
    }

    @Test
    void testDelete() {
        cardInfoService.delete(1L);
        verify(cardInfoRepository).deleteById(1L);
    }


    @Test
    void testSearchCards() {
        CardInfo c1 = new CardInfo();
        c1.setCardNumber("1234");
        CardInfo c2 = new CardInfo();
        c2.setCardNumber("5678");

        Pageable pageable = PageRequest.of(0, 10);

        CardInfoDto dto1 = new CardInfoDto(1L, 1L, "1234", "John", LocalDate.now());
        CardInfoDto dto2 = new CardInfoDto(2L, 1L, "5678", "Jane", LocalDate.now());

        Page<CardInfo> page = new PageImpl<>(List.of(c1, c2), pageable, 2);
        when(cardInfoRepository.findAll((Specification<CardInfo>) any(), eq(pageable)))
                .thenReturn(page);

        when(cardInfoMapper.toDto(c1)).thenReturn(dto1);
        when(cardInfoMapper.toDto(c2)).thenReturn(dto2);

        Page<CardInfoDto> result = cardInfoService.searchCards(1L, null, null, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("1234", result.getContent().get(0).cardNumber());
        assertEquals("5678", result.getContent().get(1).cardNumber());
    }

}
