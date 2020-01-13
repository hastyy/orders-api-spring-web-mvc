package com.example.ordersapi.user.controller;

import com.example.ordersapi.user.api.UserAPI;
import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;
import com.example.ordersapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;

    @Override
    public User create(@Valid UserDto userDto) throws EmailAlreadyInUseException {
        log.info("Registering user: {}", userDto.getEmail());

        User registeredUser = userService.registerUser(userDto);

        log.info("Registered user: {}", registeredUser.getEmail());

        return registeredUser;
    }

}
