package com.example.ordersapi.authentication.filter;

import com.example.ordersapi.authentication.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION_HEADER_KEY);

        if (hasText(header) && header.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            String jwt = header.substring(AUTHORIZATION_HEADER_PREFIX.length());
            Authentication establishedPrincipal = SecurityContextHolder.getContext().getAuthentication();

            try {

                if (!jwtService.isTokenExpired(jwt) && establishedPrincipal == null) {

                    String username = jwtService.extractUsername(jwt);

                    try {

                        UserDetails principal = userDetailsService.loadUserByUsername(username);

                        setRequestSession(request, principal);

                    } catch (UsernameNotFoundException exception) {
                        log.warn("Could not find user: {} extracted from jwt: {}", username, jwt);
                    }
                }

            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT: {} failed: {}", jwt, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT: {} failed: {}", jwt, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT: {} failed: {}", jwt, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature: {} failed: {}", jwt, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT: {} failed: {}", jwt, exception.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setRequestSession(HttpServletRequest request, UserDetails principal) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal,
                null, principal.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
