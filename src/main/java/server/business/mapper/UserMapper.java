package server.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import server.data.entity.User;
import server.presentation.dto.request.CreateUserDto;
import server.presentation.dto.request.UpdateUserDto;
import server.presentation.dto.response.UserDto;

@Mapper(componentModel = "spring", uses = {CardInfoMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", source = "cards")
    User toUser(CreateUserDto dto);

    @Mapping(target = "birthdate", source = "birthDate")
    @Mapping(target = "cards", source = "cards")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);
}
