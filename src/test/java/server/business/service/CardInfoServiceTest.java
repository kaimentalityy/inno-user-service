package server.business.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.business.mapper.CardInfoMapper;
import server.data.entity.CardInfo;
import server.data.repository.CardInfoRepository;
import server.presentation.dto.request.CreateCardInfoDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.response.CardInfoDto;
import server.util.exceptions.notfound.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private CardInfoMapper cardInfoMapper;

    @InjectMocks
    private CardInfoService cardInfoService;

    public CardInfoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCardInfo() {
        CreateCardInfoDto dto = new CreateCardInfoDto("1234", "John Doe", LocalDate.now());
        CardInfo cardEntity = new CardInfo();
        CardInfoDto cardDto = new CardInfoDto(1L, "1234", "John Doe", LocalDate.now());

        when(cardInfoMapper.toEntity(dto)).thenReturn(cardEntity);
        when(cardInfoRepository.save(cardEntity)).thenReturn(cardEntity);
        when(cardInfoMapper.toDto(cardEntity)).thenReturn(cardDto);

        CardInfoDto result = cardInfoService.createCardInfo(dto);

        assertNotNull(result);
        assertEquals("1234", result.cardNumber());
        verify(cardInfoRepository).save(cardEntity);
    }

    @Test
    void testUpdateCardInfo() {
        UpdateCardInfoDto dto = new UpdateCardInfoDto(1L, "5678", "Jane Doe", LocalDate.now());
        CardInfo cardEntity = new CardInfo();

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(cardEntity));
        when(cardInfoRepository.save(cardEntity)).thenReturn(cardEntity);
        when(cardInfoMapper.toDto(cardEntity)).thenReturn(new CardInfoDto(1L, "5678", "Jane Doe", LocalDate.now()));

        CardInfoDto result = cardInfoService.updateCardInfo(1L, dto);

        assertNotNull(result);
        assertEquals("5678", result.cardNumber());
        verify(cardInfoRepository).save(cardEntity);
    }

    @Test
    void testGetCardInfo() {
        CardInfoRepository cardInfoRepository = mock(CardInfoRepository.class);
        CardInfoMapper cardInfoMapper = mock(CardInfoMapper.class);
        CardInfoService cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper);

        CardInfo card = new CardInfo();
        card.setId(1L);
        card.setCardNumber("1234-5678-9012-3456");

        CardInfoDto cardDto = new CardInfoDto(1L, "1234-5678-9012-3456", "Holder", LocalDate.now());

        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardInfoMapper.toDto(card)).thenReturn(cardDto);

        CardInfoDto result = cardInfoService.getCardInfo(1L);

        assertNotNull(result);
        assertEquals(card.getCardNumber(), result.cardNumber());
    }

    @Test
    void testGetCardInfoNotFound() {
        CardInfoRepository cardInfoRepository = mock(CardInfoRepository.class);
        CardInfoMapper cardInfoMapper = mock(CardInfoMapper.class);
        CardInfoService cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper);

        when(cardInfoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                cardInfoService.getCardInfo(999L)
        );
    }

    @Test
    void testGetCardsByUserId() {
        CardInfoRepository cardInfoRepository = mock(CardInfoRepository.class);
        CardInfoMapper cardInfoMapper = mock(CardInfoMapper.class);
        CardInfoService cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper);

        CardInfo card1 = new CardInfo();
        card1.setId(1L);
        card1.setCardNumber("1234-5678-9012-3456");

        CardInfo card2 = new CardInfo();
        card2.setId(2L);
        card2.setCardNumber("9999-8888-7777-6666");

        CardInfoDto dto1 = new CardInfoDto(1L, "1234-5678-9012-3456", "Holder1", LocalDate.now());
        CardInfoDto dto2 = new CardInfoDto(2L, "9999-8888-7777-6666", "Holder2", LocalDate.now());

        when(cardInfoRepository.findByUserId(1L)).thenReturn(Arrays.asList(card1, card2));
        when(cardInfoMapper.toDto(card1)).thenReturn(dto1);
        when(cardInfoMapper.toDto(card2)).thenReturn(dto2);

        List<CardInfoDto> result = cardInfoService.getCardsByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("1234-5678-9012-3456", result.get(0).cardNumber());
        assertEquals("9999-8888-7777-6666", result.get(1).cardNumber());
    }

    @Test
    void testGetCardsByIds() {
        CardInfoRepository cardInfoRepository = mock(CardInfoRepository.class);
        CardInfoMapper cardInfoMapper = mock(CardInfoMapper.class);
        CardInfoService cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper);

        CardInfo card1 = new CardInfo();
        card1.setId(1L);
        card1.setCardNumber("1234-5678-9012-3456");

        CardInfo card2 = new CardInfo();
        card2.setId(2L);
        card2.setCardNumber("9999-8888-7777-6666");

        CardInfoDto dto1 = new CardInfoDto(1L, "1234-5678-9012-3456", "Holder1", LocalDate.now());
        CardInfoDto dto2 = new CardInfoDto(2L, "9999-8888-7777-6666", "Holder2", LocalDate.now());

        List<Long> ids = Arrays.asList(1L, 2L);

        when(cardInfoRepository.findCardsByIds(ids)).thenReturn(Arrays.asList(card1, card2));
        when(cardInfoMapper.toDto(card1)).thenReturn(dto1);
        when(cardInfoMapper.toDto(card2)).thenReturn(dto2);

        List<CardInfoDto> result = cardInfoService.getCardsByIds(ids);

        assertEquals(2, result.size());
        assertEquals("1234-5678-9012-3456", result.get(0).cardNumber());
        assertEquals("9999-8888-7777-6666", result.get(1).cardNumber());
    }

    @Test
    void testGetCardByNumber() {
        CardInfoRepository cardInfoRepository = mock(CardInfoRepository.class);
        CardInfoMapper cardInfoMapper = mock(CardInfoMapper.class);
        CardInfoService cardInfoService = new CardInfoService(cardInfoRepository, cardInfoMapper);

        CardInfo card = new CardInfo();
        card.setId(1L);
        card.setCardNumber("1234-5678-9012-3456");

        CardInfoDto cardDto = new CardInfoDto(1L, "1234-5678-9012-3456", "Holder", LocalDate.now());

        when(cardInfoRepository.findByCardNumberNative("1234-5678-9012-3456")).thenReturn(card);
        when(cardInfoMapper.toDto(card)).thenReturn(cardDto);

        CardInfoDto result = cardInfoService.getCardByNumber("1234-5678-9012-3456");

        assertNotNull(result);
        assertEquals(card.getCardNumber(), result.cardNumber());
    }

    @Test
    void testGetCardByNumberNotFound() {
        when(cardInfoRepository.findByCardNumberNative("0000-0000-0000-0000")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () ->
                cardInfoService.getCardByNumber("0000-0000-0000-0000")
        );
    }

    @Test
    void testDeleteCardInfo() {
        cardInfoService.deleteCardInfo(1L);
        verify(cardInfoRepository).deleteById(1L);
    }
}
