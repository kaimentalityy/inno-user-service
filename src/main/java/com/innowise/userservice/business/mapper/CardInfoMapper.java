package com.innowise.userservice.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.presentation.dto.request.CreateCardInfoDto;
import com.innowise.userservice.presentation.dto.request.UpdateCardInfoDto;
import com.innowise.userservice.presentation.dto.response.CardInfoDto;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cardHolder", source = "cardHolderName")
    CardInfo toEntity(CreateCardInfoDto dto);

    CardInfoDto toDto(CardInfo entity);

    @Mapping(target = "user", ignore = true)
    void updateEntity(UpdateCardInfoDto dto, @MappingTarget CardInfo entity);
}
