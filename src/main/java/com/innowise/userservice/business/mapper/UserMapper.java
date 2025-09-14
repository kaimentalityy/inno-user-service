package com.innowise.userservice.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.presentation.dto.request.CreateUserDto;
import com.innowise.userservice.presentation.dto.request.UpdateUserDto;
import com.innowise.userservice.presentation.dto.response.UserDto;

@Mapper(componentModel = "spring", uses = {CardInfoMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", source = "cards")
    @Mapping(target = "birthDate", source = "birthdate")
    User toUser(CreateUserDto dto);

    @Mapping(target = "birthdate", source = "birthDate")
    @Mapping(target = "cards", source = "cards")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);
}
