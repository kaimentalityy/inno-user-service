package com.innowise.userservice.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.innowise.userservice.data.entity.CardInfo;
import com.innowise.userservice.presentation.dto.CardInfoDto;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CardInfo toEntity(CardInfoDto dto);

    @Mapping(target = "user", ignore = true)
    void updateEntity(CardInfoDto dto, @MappingTarget CardInfo entity);

    @Mapping(target = "userId", source = "user.id")
    CardInfoDto toDto(CardInfo entity);
}
