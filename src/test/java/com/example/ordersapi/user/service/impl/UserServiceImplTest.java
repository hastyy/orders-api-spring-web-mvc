package com.example.ordersapi.user.service.impl;

import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;
import com.example.ordersapi.user.mapper.UserMapper;
import com.example.ordersapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void registerUser_registers_user_successfully_when_email_not_in_use() throws Exception {
        // given
        final Integer ID = 1;
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final String ENCODED_PASSWORD = "73bd68e2fe18622931676e4d020e9718efe84985";

        UserDto userDto = new UserDto();
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);

        User mappedUser = new User();
        mappedUser.setEmail(EMAIL);
        mappedUser.setPassword(PASSWORD);

        User encodedPasswordUser = new User();
        encodedPasswordUser.setEmail(EMAIL);
        encodedPasswordUser.setPassword(ENCODED_PASSWORD);

        User expectedUser = new User();
        expectedUser.setId(ID);
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(ENCODED_PASSWORD);

        // when
        when(userMapper.userDtoToUser(userDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.saveAndFlush(encodedPasswordUser)).thenReturn(expectedUser);

        // then
        User savedUser = userService.registerUser(userDto);
        assertThat(savedUser, equalTo(expectedUser));
        verify(userMapper, times(1)).userDtoToUser(userDto);
        verifyNoMoreInteractions(userMapper);
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository, times(1)).saveAndFlush(encodedPasswordUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void registerUser_fails_to_register_user_when_email_already_in_use() throws Exception {
        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final String ENCODED_PASSWORD = "73bd68e2fe18622931676e4d020e9718efe84985";

        UserDto userDto = new UserDto();
        userDto.setEmail(EMAIL);
        userDto.setPassword(PASSWORD);

        User mappedUser = new User();
        mappedUser.setEmail(EMAIL);
        mappedUser.setPassword(PASSWORD);

        User encodedPasswordUser = new User();
        encodedPasswordUser.setEmail(EMAIL);
        encodedPasswordUser.setPassword(ENCODED_PASSWORD);

        // when
        when(userMapper.userDtoToUser(userDto)).thenReturn(mappedUser);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.saveAndFlush(encodedPasswordUser)).thenThrow(DataIntegrityViolationException.class);

        // then
        assertThrows(EmailAlreadyInUseException.class, () -> userService.registerUser(userDto));
        verify(userMapper, times(1)).userDtoToUser(userDto);
        verifyNoMoreInteractions(userMapper);
        verify(passwordEncoder, times(1)).encode(PASSWORD);
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository, times(1)).saveAndFlush(encodedPasswordUser);
        verifyNoMoreInteractions(userRepository);
    }

}