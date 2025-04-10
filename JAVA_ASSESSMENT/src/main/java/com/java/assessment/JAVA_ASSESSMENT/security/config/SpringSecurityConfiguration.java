package com.java.assessment.JAVA_ASSESSMENT.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.assessment.JAVA_ASSESSMENT.model.response.BasicRestResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Timestamp;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableWebMvc
@EnableMethodSecurity
public class SpringSecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;

    private final CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/swagger-resources/**",      // Note the correction from "swagger/resources"
            "/swagger-ui/**",
            "/swagger-ui.html",            // Add direct access to Swagger UI HTML page
            "/v3/api-docs/**",             // Allow access to OpenAPI documentation
            "/webjars/**",
            "/api/v1/phoneNumberVerification/**",
            "/api/v1/auth/loginPage",
            "/oauth2/authorization/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
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
