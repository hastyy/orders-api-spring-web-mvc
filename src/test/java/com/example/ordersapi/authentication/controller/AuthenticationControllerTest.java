package com.example.ordersapi.authentication.controller;

import com.example.ordersapi.authentication.api.AuthenticationAPI;
import com.example.ordersapi.authentication.api.dto.AuthCredentials;
import com.example.ordersapi.authentication.api.dto.AuthToken;
import com.example.ordersapi.authentication.model.Principal;
import com.example.ordersapi.testutils.SecurityEnabledTest;
import com.example.ordersapi.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest extends SecurityEnabledTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    // Bean already mocked in superclass
    // @MockBean
    // private JwtService jwtService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void authenticate_should_return_jwt_on_successful_authentication() throws Exception {
        final String JWT_TOKEN = "THIS-SHOULD-BE-A-JWT-TOKEN";

        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final AuthCredentials authCredentials = new AuthCredentials(EMAIL, PASSWORD);

        // when
        final Authentication credentials = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);
        final Authentication principal = getPrincipal(EMAIL, PASSWORD);
        when(authenticationManager.authenticate(credentials)).thenReturn(principal);
        when(jwtService.generateToken(principal)).thenReturn(JWT_TOKEN);

        // then
        MvcResult response = mockMvc.perform(post(AuthenticationAPI.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(authCredentials)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        AuthToken returnedToken = jsonMapper.readValue(responseBodyJson, AuthToken.class);

        assertThat(returnedToken, is(equalTo(AuthToken.of(JWT_TOKEN))));

        verify(authenticationManager, times(1)).authenticate(credentials);
        verifyNoMoreInteractions(authenticationManager);

        verify(jwtService, times(1)).generateToken(principal);
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void authenticate_should_return_unauthorized_on_unsuccessful_authentication() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final AuthCredentials wrongAuthCredentials = new AuthCredentials(EMAIL, PASSWORD);

        // when
        final Authentication wrongCredentials = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);
        when(authenticationManager.authenticate(wrongCredentials)).thenThrow(new BadCredentialsException(""));

        // then
        mockMvc.perform(post(AuthenticationAPI.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(wrongAuthCredentials)))
            .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(wrongCredentials);
        verifyNoMoreInteractions(authenticationManager);

        verifyNoInteractions(jwtService);
    }

    @Test
    void authenticate_should_return_bad_request_when_credentials_have_invalid_email() throws Exception {
        // given
        final String BAD_EMAIL = "test_test.com";
        final String PASSWORD = "test_password";
        final AuthCredentials badAuthCredentials = new AuthCredentials(BAD_EMAIL, PASSWORD);

        // when

        // then
        mockMvc.perform(post(AuthenticationAPI.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(badAuthCredentials)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }

    @Test
    void authenticate_should_return_bad_request_when_credentials_are_missing_password() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final AuthCredentials badAuthCredentials = new AuthCredentials(EMAIL, null);

        // when

        // then
        mockMvc.perform(post(AuthenticationAPI.BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(badAuthCredentials)))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }

    private Authentication getPrincipal(String username, String password) {
        User userEntity = new User();
        userEntity.setEmail(username);
        userEntity.setPassword(password);
        return new UsernamePasswordAuthenticationToken(new Principal(userEntity), null, Collections.emptyList());
    }

}