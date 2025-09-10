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

import java.time.LocalDate;
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
    void testDeleteCardInfo() {
        cardInfoService.deleteCardInfo(1L);
        verify(cardInfoRepository).deleteById(1L);
    }
}
