package com.innowise.userservice.mapper;

import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserDto dto);

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto dto, @MappingTarget User user);
}
