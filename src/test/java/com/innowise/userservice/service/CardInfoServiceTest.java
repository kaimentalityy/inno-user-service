package com.innowise.userservice.service;

import com.innowise.userservice.exception.EntityNotFoundException;
import com.innowise.userservice.mapper.CardInfoMapper;
import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.model.entity.CardInfo;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.dao.CardInfoRepository;
import com.innowise.userservice.repository.dao.UserRepository;
import com.innowise.userservice.service.impl.CardInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cacheManager = new ConcurrentMapCacheManager("cards");
    }

    @Test
    void createCard_success() {
        CardInfoDto dto = new CardInfoDto(null, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        User user = new User();
        user.setId(1L);

        CardInfo cardEntity = new CardInfo();
        CardInfo savedEntity = new CardInfo();
        savedEntity.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardInfoMapper.toEntity(dto)).thenReturn(cardEntity);
        when(cardInfoRepository.save(cardEntity)).thenReturn(savedEntity);
        when(cardInfoMapper.toDto(savedEntity))
                .thenReturn(new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1)));

        CardInfoDto result = cardInfoService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(userRepository).findById(1L);
        verify(cardInfoMapper).toEntity(dto);
        verify(cardInfoRepository).save(cardEntity);
        verify(cardInfoMapper).toDto(savedEntity);
    }

    @Test
    void createCard_userNotFound() {
        CardInfoDto dto = new CardInfoDto(null, 999L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.create(dto));
        verify(userRepository).findById(999L);
        verifyNoInteractions(cardInfoRepository);
    }

    @Test
    void findById_success() {
        CardInfo entity = new CardInfo();
        entity.setId(1L);

        CardInfoDto dto = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(cardInfoMapper.toDto(entity)).thenReturn(dto);

        CardInfoDto result = cardInfoService.findById(1L);

        assertEquals(1L, result.id());
        verify(cardInfoRepository).findById(1L);
        verify(cardInfoMapper).toDto(entity);
    }

    @Test
    void findById_notFound() {
        when(cardInfoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.findById(1L));
    }

    @Test
    void deleteCard_success() {
        when(cardInfoRepository.existsById(1L)).thenReturn(true);

        cardInfoService.delete(1L);

        verify(cardInfoRepository).deleteById(1L);
    }

    @Test
    void deleteCard_notFound() {
        when(cardInfoRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> cardInfoService.delete(1L));
        verify(cardInfoRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByIds_success() {
        CardInfo card = new CardInfo();
        card.setId(1L);

        CardInfoDto dto = new CardInfoDto(1L, 1L, "1234567890123", "John Doe", LocalDate.now().plusDays(1));

        when(cardInfoRepository.findAllById(List.of(1L))).thenReturn(List.of(card));
        when(cardInfoMapper.toDto(card)).thenReturn(dto);

        var list = cardInfoService.findByIds(List.of(1L));

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).id());
    }
}
