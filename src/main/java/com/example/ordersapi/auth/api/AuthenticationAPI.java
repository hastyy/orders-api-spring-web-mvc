package com.example.ordersapi.auth.api;

import com.example.ordersapi.auth.api.dto.AuthenticationToken;
import com.example.ordersapi.auth.api.dto.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(AuthenticationAPI.BASE_URL)
public interface AuthenticationAPI {

    String BASE_URL = "/authenticate";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    AuthenticationToken authenticate(@RequestBody UserCredentials userCredentials) throws AuthenticationException;

}
