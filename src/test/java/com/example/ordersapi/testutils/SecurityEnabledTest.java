package com.example.ordersapi.testutils;

import com.example.ordersapi.authentication.service.JwtService;
import com.example.ordersapi.common.configuration.SecurityConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;

@Import(SecurityConfiguration.class)
public abstract class SecurityEnabledTest {

    /**
     * Mocked bean because it's a dependency of the SecurityConfiguration
     */
    @MockBean
    protected UserDetailsService userDetailsService;

    /**
     * Mocked bean because it's a dependency of the SecurityConfiguration
     */
    @MockBean
    protected JwtService jwtService;

}
