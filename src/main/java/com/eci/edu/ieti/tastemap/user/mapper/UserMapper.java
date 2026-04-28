package com.eci.edu.ieti.tastemap.user.mapper;

import com.eci.edu.ieti.tastemap.user.dto.UserRequestDto;
import com.eci.edu.ieti.tastemap.user.dto.UserResponseDto;
import com.eci.edu.ieti.tastemap.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between User, UserRequestDto, and UserResponseDto.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    User toUser(UserRequestDto userRequestDto);

    UserResponseDto toUserResponseDto(User user);
}

