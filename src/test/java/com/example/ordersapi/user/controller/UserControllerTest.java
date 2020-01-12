package com.example.ordersapi.user.controller;

import com.example.ordersapi.user.api.UserAPI;
import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;
import com.example.ordersapi.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create_should_return_registered_user_when_request_is_valid() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final UserDto userDto = buildDto(EMAIL, PASSWORD);
        final User expectedUser = buildUser(EMAIL, PASSWORD);

        // when
        when(userService.registerUser(userDto)).thenReturn(expectedUser);

        // then
        MvcResult response = mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyJson = response.getResponse().getContentAsString();
        User responseUser = jsonMapper.readValue(responseBodyJson, User.class);

        assertThat(responseUser, is(equalTo(expectedUser)));
        verify(userService, times(1)).registerUser(userDto);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void create_should_return_conflict_when_request_valid_but_email_in_use() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final UserDto userDto = buildDto(EMAIL, PASSWORD);

        // when
        when(userService.registerUser(userDto)).thenThrow(new EmailAlreadyInUseException(EMAIL));

        // then
        mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).registerUser(userDto);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void create_should_return_bad_request_when_request_has_invalid_email() throws Exception {
        // given
        final String BAD_EMAIL = "test_test.com";
        final String PASSWORD = "test_password";
        final UserDto userDto = buildDto(BAD_EMAIL, PASSWORD);

        // when

        // then
        mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void create_should_return_bad_request_when_request_has_invalid_password() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final String BAD_PASSWORD = "";
        final UserDto userDto = buildDto(EMAIL, BAD_PASSWORD);

        // when

        // then
        mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void create_should_return_bad_request_when_request_is_missing_email() throws Exception {
        // given
        final String PASSWORD = "test_password";
        final UserDto userDto = buildDto(null, PASSWORD);

        // when

        // then
        mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void create_should_return_bad_request_when_request_is_missing_password() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final UserDto userDto = buildDto(EMAIL, null);

        // when

        // then
        mockMvc.perform(post(UserAPI.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    private UserDto buildDto(String email, String password) {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }

    private User buildUser(String email, String password){
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

}