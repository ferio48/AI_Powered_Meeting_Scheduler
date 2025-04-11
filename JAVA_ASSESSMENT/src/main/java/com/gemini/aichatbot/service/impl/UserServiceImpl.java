package com.gemini.aichatbot.service.impl;

import com.gemini.aichatbot.constant.AppConstants;
import com.gemini.aichatbot.enums.Role;
import com.gemini.aichatbot.exception.ResourceNotFoundException;
import com.gemini.aichatbot.exception.UserAlreadyPresentException;
import com.gemini.aichatbot.exception.ValidationException;
import com.gemini.aichatbot.model.request.RegisterUserRequest;
import com.gemini.aichatbot.security.config.JwtUtil;
import com.gemini.aichatbot.security.model.AuthenticationResponse;
import com.gemini.aichatbot.security.service.AuthenticationService;
import com.gemini.aichatbot.util.LocaleUtil;
import com.gemini.aichatbot.util.PasswordUtil;
import com.gemini.aichatbot.constant.ErrorConstants;
import com.gemini.aichatbot.mapper.UserMapper;
import com.gemini.aichatbot.model.dto.UserDto;
import com.gemini.aichatbot.model.entity.User;
import com.gemini.aichatbot.repositories.TokenRepository;
import com.gemini.aichatbot.repositories.UserRepository;
import com.gemini.aichatbot.service.UserService;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service implementation for user-related operations.
 *
 * Handles:
 * - Fetching user details by username
 * - Registering new users via email
 * - Password validation and encoding
 * - Localization of error messages
 * - Token and authentication management
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /** Password encoder for encrypting user passwords */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** Repository for accessing user data */
    @Autowired
    private UserRepository userRepository;

    /** Mapper for converting between User and UserDto */
    @Autowired
    private UserMapper userMapper;

    /** Source for error-related localized messages */
    @Qualifier("errorMessageSource")
    @Autowired
    private MessageSource errorMessageSource;

    /** Source for general localized messages */
    @Qualifier("generalMessageSource")
    @Autowired
    private MessageSource generalMessageSource;

    /** Service for authenticating users and generating JWTs */
    @Autowired
    private AuthenticationService authenticationService;

    /** Utility for JWT generation and validation */
    @Autowired
    private JwtUtil jwtUtil;

    /** Token expiration time (in milliseconds) for password resets */
    @Value("${security.resetPasswordToken.expiration.time}")
    private Long resetPasswordTokenExpirationTime;

    /** Repository for managing JWT tokens */
    @Autowired
    private TokenRepository tokenRepository;

    /**
     * Retrieves user information based on their username.
     *
     * @param userName the username of the user
     * @return UserDto containing user details
     * @throws ResourceNotFoundException if no user is found with the given username
     */
    @Override
    public UserDto findByUsername(String userName) {
        log.info("Entered method to find User with the given username: {}", userName);
        UserDto userDto = null;

        if (userName != null) {
            log.info("Making call to user repository for finding user with the given username: {}", userName);
            User userEntity = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not FOUND!!!"));

            log.info("User found successfully for the given username: {}", userName);
            userDto = userMapper.mapToUserDto(UserDto.builder().build(), userEntity);
            log.info("User successfully mapped to userDto: {}", userDto);
        }

        log.info("Returning userDto from findByUsername method: {}", userDto);
        return userDto;
    }

    /**
     * Registers a new user using email, phone, and password (base64 encoded).
     *
     * - Validates email uniqueness
     * - Decodes and validates passwords
     * - Encrypts password using PasswordEncoder
     * - Persists the new user
     * - Generates an authentication token on success
     *
     * @param registerUserRequest contains registration details (email, name, phone, password, confirmPassword)
     * @return AuthenticationResponse containing JWT token, success/failure status, and message
     */
    @Override
    public AuthenticationResponse registerWithEmail(RegisterUserRequest registerUserRequest) {
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().build();
        List<String> errorList = new ArrayList<>();

        try {
            // Check for existing user
            final Optional<User> optionalUser = userRepository.findByEmailAddress(registerUserRequest.getEmailAddress());
            if (optionalUser.isPresent())
                throw new UserAlreadyPresentException(errorMessageSource.getMessage(ErrorConstants.USER_PRESENT_EMAIL_ADDRESS_ERROR, null, LocaleUtil.getLocale()));

            // Decode and validate password
            byte[] keyBytesForPassword = Decoders.BASE64.decode(registerUserRequest.getPassword());
            String password = new String(keyBytesForPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(password))
                errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_FORMAT_ERROR, new Object[]{"New Password"}, LocaleUtil.getLocale()));

            // Decode and validate confirm password
            byte[] keyBytesForConfirmPassword = Decoders.BASE64.decode(registerUserRequest.getConfirmPassword());
            String confirmPassword = new String(keyBytesForConfirmPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(confirmPassword))
                errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_FORMAT_ERROR, new Object[]{"Confirm Password"}, LocaleUtil.getLocale()));

            if (password.isBlank())
                errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_BLANK, new Object[]{password}, LocaleUtil.getLocale()));

            if (confirmPassword.isBlank())
                errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_BLANK, new Object[]{confirmPassword}, LocaleUtil.getLocale()));

            if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword()))
                errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELDS_NOT_EQUAL_ERROR, new Object[]{password, confirmPassword}, LocaleUtil.getLocale()));

            if (!errorList.isEmpty())
                throw new ValidationException("Error occurred");

            // Build and save user
            final User user = User.builder()
                    .username(registerUserRequest.getEmailAddress())
                    .emailAddress(registerUserRequest.getEmailAddress())
                    .name(registerUserRequest.getName())
                    .role(Role.USER)
                    .phoneNumber(registerUserRequest.getPhoneNumber())
                    .password(passwordEncoder.encode(password))
                    .build();

            final User savedUser = userRepository.save(user);

            // Generate authentication response
            authenticationResponse = authenticationService.generateAuthenticationResponse(savedUser.getUsername(), AuthenticationResponse.builder().build());
            authenticationResponse.setStatus(HttpStatus.OK.value());
            authenticationResponse.setMessage(AppConstants.UserConstants.USER_SAVED_SUCCESSFULLY);

        } catch (Exception ex) {
            authenticationResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            authenticationResponse.setMessage(ex.getLocalizedMessage());
            authenticationResponse.setErrorList(errorList);
        }

        return authenticationResponse;
    }
}