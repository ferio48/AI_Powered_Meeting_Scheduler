package com.java.assessment.JAVA_ASSESSMENT.service.impl;

import com.java.assessment.JAVA_ASSESSMENT.constant.AppConstants;
import com.java.assessment.JAVA_ASSESSMENT.constant.ErrorConstants;
import com.java.assessment.JAVA_ASSESSMENT.enums.Role;
import com.java.assessment.JAVA_ASSESSMENT.exception.ResourceNotFoundException;
import com.java.assessment.JAVA_ASSESSMENT.exception.UserAlreadyPresentException;
import com.java.assessment.JAVA_ASSESSMENT.exception.ValidationException;
import com.java.assessment.JAVA_ASSESSMENT.mapper.UserMapper;
import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.model.request.RegisterUserRequest;
import com.java.assessment.JAVA_ASSESSMENT.repositories.TokenRepository;
import com.java.assessment.JAVA_ASSESSMENT.repositories.UserRepository;
import com.java.assessment.JAVA_ASSESSMENT.security.config.JwtUtil;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationResponse;
import com.java.assessment.JAVA_ASSESSMENT.security.service.AuthenticationService;
import com.java.assessment.JAVA_ASSESSMENT.service.UserService;
import com.java.assessment.JAVA_ASSESSMENT.util.LocaleUtil;
import com.java.assessment.JAVA_ASSESSMENT.util.PasswordUtil;
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

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Qualifier("errorMessageSource")
    @Autowired
    private MessageSource errorMessageSource;

    @Qualifier("generalMessageSource")
    @Autowired
    private MessageSource generalMessageSource;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${security.resetPasswordToken.expiration.time}")
    private Long resetPasswordTokenExpirationTime;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDto findByUsername(String userName) {
        log.info("Entered method to find User with the given username: {}", userName);
        UserDto userDto = null;
        if(null != userName) {
            log.info("Making call to user repository for finding user with the given username: {}", userName);
            User userEntity = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not FOUND!!!"));
            if(null != userEntity) {
                log.info("User found successfully for the given username: {}", userName);
                userDto = userMapper.mapToUserDto(UserDto.builder().build(), userEntity);
                log.info("User successfully mapped to userDto: {}", userDto);
            }
        }
        log.info("Returning userDto from findByUsername method: {}", userDto);
        return userDto;
    }

    @Override
    public AuthenticationResponse registerWithEmail(RegisterUserRequest registerUserRequest) {
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder().build();
        List<String> errorList = new ArrayList<>();
        try {

            final Optional<User> optionalUser = userRepository.findByEmailAddress(registerUserRequest.getEmailAddress());
            if(optionalUser.isPresent()) throw new UserAlreadyPresentException(errorMessageSource.getMessage(ErrorConstants.USER_PRESENT_EMAIL_ADDRESS_ERROR, null, LocaleUtil.getLocale()));

            byte[] keyBytesForPassword = Decoders.BASE64.decode(registerUserRequest.getPassword());
            String password = new String(keyBytesForPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(password)) errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_FORMAT_ERROR, new Object[] {"New Password"}, LocaleUtil.getLocale()));

            byte[] keyBytesForConfirmPassword = Decoders.BASE64.decode(registerUserRequest.getConfirmPassword());
            String confirmPassword = new String(keyBytesForConfirmPassword, StandardCharsets.UTF_8);
            if (PasswordUtil.isNotInPasswordFormat(confirmPassword)) errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_FORMAT_ERROR, new Object[] {"Confirm Password"}, LocaleUtil.getLocale()));

            if (password.isBlank()) errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_BLANK, new Object[]{password}, LocaleUtil.getLocale()));
            if (confirmPassword.isBlank()) errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELD_BLANK, new Object[]{confirmPassword}, LocaleUtil.getLocale()));

            if (!registerUserRequest.getPassword().equals(registerUserRequest.getConfirmPassword())) errorList.add(errorMessageSource.getMessage(ErrorConstants.FIELDS_NOT_EQUAL_ERROR, new Object[] {password, confirmPassword}, LocaleUtil.getLocale()));

            if(!errorList.isEmpty()) throw new ValidationException("Error occurred");

            final User user = User
                    .builder()
                    .username(registerUserRequest.getEmailAddress())
                    .emailAddress(registerUserRequest.getEmailAddress())
                    .name(registerUserRequest.getName())
                    .role(Role.USER)
                    .phoneNumber(registerUserRequest.getPhoneNumber())
                    .password(passwordEncoder.encode(password))
                    .build();

            final User savedUser = userRepository.save(user);

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
