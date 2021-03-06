package com.example.ordersapi.common.configuration;

import com.example.ordersapi.authentication.filter.AuthorizationFilter;
import com.example.ordersapi.order.api.OrderAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    private final UserDetailsService userDetailsService;
    private final AuthorizationFilter authorizationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (h2ConsoleEnabled) {
            http.authorizeRequests()
                .antMatchers("/h2-console", "/h2-console/**").permitAll()
                .and()
                .headers().frameOptions().sameOrigin();
        }

        http.cors().and().csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler())
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers(OrderAPI.BASE_URL + "/**").authenticated()
                .anyRequest().permitAll();

                /*
                .antMatchers(AuthenticationAPI.BASE_URL + "/**").permitAll()
                .antMatchers(ProductAPI.BASE_URL + "/**").permitAll()
                .antMatchers(UserAPI.BASE_URL + "/**").permitAll()
            .anyRequest().authenticated();

                 */

        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, e) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    /**
     * We need to override this method in order to add the @Bean annotation because Spring doesn't create an AuthenticationManager bean by default anymore.
     * Without this we can't wire AuthenticationManager in other beans.
     * @return AuthenticationManager bean
     * @throws Exception on unsuccessful bean creation
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
