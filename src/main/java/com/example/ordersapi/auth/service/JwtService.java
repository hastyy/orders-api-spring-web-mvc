package com.example.ordersapi.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {

    String generateToken(Authentication authentication);

    String extractUsername(String token);

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenExpired(String token);
}
