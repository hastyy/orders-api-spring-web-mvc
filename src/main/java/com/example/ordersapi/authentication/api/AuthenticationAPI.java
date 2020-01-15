package com.example.ordersapi.authentication.api;

import com.example.ordersapi.authentication.api.dto.AuthCredentials;
import com.example.ordersapi.authentication.api.dto.AuthToken;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(AuthenticationAPI.BASE_URL)
public interface AuthenticationAPI {

    String BASE_URL = "/api/v1/authenticate";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    AuthToken authenticate(@Valid @RequestBody AuthCredentials authCredentials) throws AuthenticationException;

}
