package com.example.ordersapi.authentication.service.impl;

import com.example.ordersapi.authentication.model.Principal;
import com.example.ordersapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceJpa implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
            .map(Principal::new)
            .orElseThrow(() -> new UsernameNotFoundException("Could not find user in the database"));
    }

}
