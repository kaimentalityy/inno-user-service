package server.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import server.data.entity.CardInfo;
import server.presentation.dto.request.CreateCardInfoDto;
import server.presentation.dto.request.UpdateCardInfoDto;
import server.presentation.dto.response.CardInfoDto;

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
