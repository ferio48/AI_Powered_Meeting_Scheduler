package com.aipowered.meeting.scheduler.security.config;

import com.aipowered.meeting.scheduler.model.response.BasicRestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Timestamp;

/**
 * Configuration class for Spring Security.
 *
 * This class sets up:
 * - Stateless session policy using JWT
 * - Custom JWT authentication filter
 * - Public and protected route management
 * - JSON-based error response for unauthorized access
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
@EnableMethodSecurity
public class SpringSecurityConfiguration {

    /** Custom authentication provider used to validate credentials */
    private final AuthenticationProvider authenticationProvider;

    /** JWT filter to extract user identity from incoming requests */
    private final CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    /**
     * List of publicly accessible (whitelisted) URLs.
     * These routes do not require authentication.
     */
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/webjars/**",
            "/api/v1/phoneNumberVerification/**",
            "/api/v1/auth/loginPage",
            "/oauth2/authorization/**",
            "/api/qna/**",
            "/meeting/**"
    };

    /**
     * Configures the HTTP security for the application.
     *
     * - Disables CSRF protection (used for stateless APIs)
     * - Allows access to public endpoints
     * - Requires authentication for all other endpoints
     * - Uses JWT for authentication via a custom filter
     * - Returns JSON response on authentication failure
     *
     * @param http the {@link HttpSecurity} object provided by Spring
     * @return a fully built {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                            BasicRestResponse restResponse = new BasicRestResponse();
                            restResponse.setMessage("Access denied");
                            restResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            restResponse.setErrorList(null);
                            restResponse.setTime(new Timestamp(System.currentTimeMillis()));
                            restResponse.setCurrentSize(0);
                            restResponse.setTotalSize(0);
                            restResponse.setContent(null);

                            ObjectMapper mapper = new ObjectMapper();
                            response.getWriter().write(mapper.writeValueAsString(restResponse));
                        })
                );

        return http.build();
    }
}