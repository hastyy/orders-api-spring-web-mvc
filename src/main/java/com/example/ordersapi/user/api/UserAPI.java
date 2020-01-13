package com.example.ordersapi.user.api;

import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@RequestMapping(UserAPI.BASE_URL)
public interface UserAPI {

    String BASE_URL = "/api/v1/users";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    User create(@Valid @RequestBody UserDto userDto) throws EmailAlreadyInUseException;

}
