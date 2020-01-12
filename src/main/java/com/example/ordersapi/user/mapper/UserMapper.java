package com.example.ordersapi.user.mapper;

import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

}
