package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.CardInfoDto;
import com.innowise.userservice.model.entity.CardInfo;
import com.innowise.userservice.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CardInfoMapperTest {

    private CardInfoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CardInfoMapper.class);
    }

    @Test
    void testToEntity() {
        CardInfoDto dto = new CardInfoDto(
                1L,
                10L,
                "1234567890123",
                "John Doe",
                LocalDate.now().plusYears(2)
        );

        CardInfo entity = mapper.toEntity(dto);

        // Id and user are ignored
        assertThat(entity.getId()).isNull();
        assertThat(entity.getUser()).isNull();
        assertThat(entity.getNumber()).isEqualTo("1234567890123");
        assertThat(entity.getHolder()).isEqualTo("John Doe");
        assertThat(entity.getExpirationDate()).isEqualTo(dto.expirationDate());
    }

    @Test
    void testUpdateEntity() {
        User user = new User();
        user.setId(10L);

        CardInfo entity = new CardInfo();
        entity.setId(5L);
        entity.setUser(user);
        entity.setNumber("1111222233334444");
        entity.setHolder("Old Holder");
        entity.setExpirationDate(LocalDate.now());

        CardInfoDto dto = new CardInfoDto(
                5L,
                10L,
                "1234567890123",
                "New Holder",
                LocalDate.now().plusYears(1)
        );

        mapper.updateEntity(dto, entity);

        // User should not change
        assertThat(entity.getUser()).isEqualTo(user);

        assertThat(entity.getNumber()).isEqualTo("1234567890123");
        assertThat(entity.getHolder()).isEqualTo("New Holder");
        assertThat(entity.getExpirationDate()).isEqualTo(dto.expirationDate());
    }

    @Test
    void testToDto() {
        User user = new User();
        user.setId(10L);

        CardInfo entity = new CardInfo();
        entity.setId(5L);
        entity.setUser(user);
        entity.setNumber("1234567890123");
        entity.setHolder("John Doe");
        entity.setExpirationDate(LocalDate.now().plusYears(2));

        CardInfoDto dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.userId()).isEqualTo(10L);
        assertThat(dto.number()).isEqualTo("1234567890123");
        assertThat(dto.holder()).isEqualTo("John Doe");
        assertThat(dto.expirationDate()).isEqualTo(entity.getExpirationDate());
    }
}
