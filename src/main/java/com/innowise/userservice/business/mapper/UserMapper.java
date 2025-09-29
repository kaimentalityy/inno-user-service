package com.innowise.userservice.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.presentation.dto.UserDto;

@Mapper(componentModel = "spring", uses = {CardInfoMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", source = "cards")
    User toUser(UserDto dto);

    @Mapping(target = "cards", source = "cards")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto dto, @MappingTarget User user);
}
