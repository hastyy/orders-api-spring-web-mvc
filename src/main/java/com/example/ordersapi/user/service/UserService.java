package com.example.ordersapi.user.service;

import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;

public interface UserService {

    User registerUser(UserDto userDto) throws EmailAlreadyInUseException;

}
