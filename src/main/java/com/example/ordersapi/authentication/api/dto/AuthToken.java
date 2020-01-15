package com.example.ordersapi.authentication.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    private String jwt;

    public static AuthToken of(String jwt) {
        return new AuthToken(jwt);
    }

}
