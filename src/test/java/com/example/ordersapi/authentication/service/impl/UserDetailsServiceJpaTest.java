package com.example.ordersapi.authentication.service.impl;

import com.example.ordersapi.authentication.model.Principal;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceJpaTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceJpa userDetailsServiceJpa;

    @Test
    void loadUserByUsername_should_return_principal_when_user_found_in_database() {
        // given
        final String EMAIL = "test@test.com";
        final String PASSWORD = "test_password";
        final User foundUser = new User();

        foundUser.setEmail(EMAIL);
        foundUser.setPassword(PASSWORD);

        final UserDetails expectedDetails = new Principal(foundUser);

        // when
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(foundUser));

        // then
        UserDetails returnedDetails = userDetailsServiceJpa.loadUserByUsername(EMAIL);

        assertThat(returnedDetails, equalTo(expectedDetails));
        verify(userRepository, times(1)).findUserByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void loadUserByUsername_should_throw_when_user_not_found_in_database() {
        // given
        final String EMAIL = "test@test.com";

        // when
        when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceJpa.loadUserByUsername(EMAIL));
        verify(userRepository, times(1)).findUserByEmail(EMAIL);
        verifyNoMoreInteractions(userRepository);
    }

}