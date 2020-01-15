package com.example.ordersapi.authentication.controller;

import com.example.ordersapi.authentication.api.AuthenticationAPI;
import com.example.ordersapi.authentication.api.dto.AuthCredentials;
import com.example.ordersapi.authentication.api.dto.AuthToken;
import com.example.ordersapi.authentication.service.JwtService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationAPI {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthToken authenticate(@Valid AuthCredentials authCredentials) throws AuthenticationException {
        Authentication credentials = new UsernamePasswordAuthenticationToken(authCredentials.getEmail(), authCredentials.getPassword());
        Authentication principal = authenticationManager.authenticate(credentials);

        String jwt = jwtService.generateToken(principal);

        return AuthToken.of(jwt);
    }

}
