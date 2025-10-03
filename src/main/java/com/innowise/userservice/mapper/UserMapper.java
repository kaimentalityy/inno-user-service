package com.innowise.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.model.dto.CardInfoDto;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    User toUser(UserDto dto);

    @Mapping(target = "cards", expression = "java(mapCards(user.getCards()))")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto dto, @MappingTarget User user);

    default List<CardInfoDto> mapCards(List<com.innowise.userservice.model.entity.CardInfo> cards) {
        if (cards == null || cards.isEmpty()) return List.of();
        return cards.stream()
                .map(card -> new CardInfoDto(
                        card.getId(),
                        card.getUser() != null ? card.getUser().getId() : null,
                        card.getNumber(),
                        card.getHolder(),
                        card.getExpirationDate()
                ))
                .toList();
    }
}
