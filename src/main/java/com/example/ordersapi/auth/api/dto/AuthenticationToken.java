package com.example.ordersapi.auth.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationToken {

    private String jwt;

    public static AuthenticationToken of(String jwt) {
        return new AuthenticationToken(jwt);
    }

}
