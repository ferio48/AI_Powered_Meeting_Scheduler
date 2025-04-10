package com.java.assessment.JAVA_ASSESSMENT.security.controller;


import com.java.assessment.JAVA_ASSESSMENT.model.request.RegisterUserRequest;
import com.java.assessment.JAVA_ASSESSMENT.repositories.UserRepository;
import com.java.assessment.JAVA_ASSESSMENT.security.config.JwtUtil;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationRequest;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationResponse;
import com.java.assessment.JAVA_ASSESSMENT.security.service.AuthenticationService;
import com.java.assessment.JAVA_ASSESSMENT.security.service.TokenService;
import com.java.assessment.JAVA_ASSESSMENT.service.UserService;
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

    @PostMapping(value = "/register", produces = {"application/json", "application/xml"})
    public ResponseEntity<AuthenticationResponse> registerUser(
            final @Valid @RequestBody RegisterUserRequest registerUserRequest
    ) {
        AuthenticationResponse response = userService.registerWithEmail(registerUserRequest);
        return ResponseEntity.ok(response);
    }

}
