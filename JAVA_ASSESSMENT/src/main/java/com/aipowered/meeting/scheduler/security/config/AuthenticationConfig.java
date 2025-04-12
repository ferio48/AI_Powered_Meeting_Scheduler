package com.aipowered.meeting.scheduler.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for authentication and security-related beans.
 *
 * Sets up user details retrieval, password encoding, authentication provider,
 * and common utility beans like {@link ObjectMapper} and {@link RestTemplate}.
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserRepository userRepository;

    /**
     * Provides a {@link UserDetailsService} that retrieves user information from the database
     * using the {@link UserRepository}.
     *
     * @return a {@link UserDetailsService} implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return (UserDetailsService) username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username NOT FOUND!!!"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(String.valueOf(user.getRole()))
                    .build();
        };
    }

    /**
     * Provides a {@link PasswordEncoder} bean using BCrypt hashing algorithm.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the {@link AuthenticationProvider} using DAO-based authentication.
     *
     * @return a configured {@link DaoAuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} for use in login/authentication flows.
     *
     * @param configuration the {@link AuthenticationConfiguration} auto-injected by Spring Security
     * @return the {@link AuthenticationManager} instance
     * @throws Exception if initialization fails
     */
    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Provides a {@link RestTemplate} bean for making HTTP requests.
     *
     * @return a {@link RestTemplate} instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Provides a singleton {@link ObjectMapper} bean for JSON processing.
     *
     * @return a configured {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
