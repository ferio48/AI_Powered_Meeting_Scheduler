package com.aipowered.meeting.scheduler.security.controller;


import com.aipowered.meeting.scheduler.model.request.RegisterUserRequest;
import com.aipowered.meeting.scheduler.repositories.UserRepository;
import com.aipowered.meeting.scheduler.security.config.JwtUtil;
import com.aipowered.meeting.scheduler.security.model.AuthenticationRequest;
import com.aipowered.meeting.scheduler.security.model.AuthenticationResponse;
import com.aipowered.meeting.scheduler.security.service.AuthenticationService;
import com.aipowered.meeting.scheduler.security.service.TokenService;
import com.aipowered.meeting.scheduler.service.UserService;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

/**
 * REST controller for authentication operations such as login and registration.
 *
 * This controller provides endpoints to:
 * - Authenticate existing users using username and password
 * - Register new users with basic information and role assignment
 *
 * Supports both JSON and XML response formats.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@CrossOrigin("*")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @Qualifier("errorMessageSource")
    private final MessageSource errorMessageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Authenticates a user and returns a JWT access token.
     *
     * The password is base64 decoded before authentication.
     *
     * @param authenticationRequest the login request containing username and password
     * @return ResponseEntity containing JWT and user info on success, or error response on failure
     */
    @PostMapping(value = "/login", produces = {"application/json", "application/xml"})
    public ResponseEntity<?> createAuthenticationToken(
            final @Valid @RequestBody AuthenticationRequest authenticationRequest
    ) {
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().build();
        try {
            byte[] keyBytes = Decoders.BASE64.decode(authenticationRequest.getPassword());
            authenticationRequest.setPassword(new String(keyBytes, StandardCharsets.UTF_8));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            authenticationResponse = authenticationService.generateAuthenticationResponse(authenticationRequest.getUsername(), authenticationResponse);
            return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
        } catch (Exception e) {
            authenticationResponse.setMessage(e.getLocalizedMessage());
            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(authenticationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Registers a new user with the given information.
     *
     * Performs:
     * - Email uniqueness check
     * - Password format and match validation
     * - User persistence
     * - Token generation
     *
     * @param registerUserRequest the registration request containing name, email, phone, and passwords
     * @return ResponseEntity with registration status and JWT token
     */
    @PostMapping(value = "/register", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthenticationResponse> registerUser(
            final @Valid @RequestBody RegisterUserRequest registerUserRequest
    ) {
        AuthenticationResponse response = userService.registerWithEmail(registerUserRequest);
        return ResponseEntity.ok(response);
    }
}
