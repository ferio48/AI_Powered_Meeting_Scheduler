package com.java.assessment.JAVA_ASSESSMENT.security.service.impl;

import com.java.assessment.JAVA_ASSESSMENT.constant.AppConstants;
import com.java.assessment.JAVA_ASSESSMENT.constant.MessageConstants;
import com.java.assessment.JAVA_ASSESSMENT.exception.ResourceNotFoundException;
import com.java.assessment.JAVA_ASSESSMENT.mapper.UserMapper;
import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.repositories.UserRepository;
import com.java.assessment.JAVA_ASSESSMENT.security.config.JwtUtil;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationResponse;
import com.java.assessment.JAVA_ASSESSMENT.security.service.AuthenticationService;
import com.java.assessment.JAVA_ASSESSMENT.security.service.TokenService;
import com.java.assessment.JAVA_ASSESSMENT.util.LocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource errorMessageSource;

    @Autowired
    @Qualifier("generalMessageSource")
    private MessageSource generalMessageSource;

    @Override
    public AuthenticationResponse generateAuthenticationResponse(String username, AuthenticationResponse response) {
        final UserDto userDto = findByUsername(username);
        final String accessToken = jwtUtil.generateToken(userDto, AppConstants.TokenType.ACCESS_TOKEN);
        final String refreshToken = jwtUtil.generateToken(userDto, AppConstants.TokenType.REFRESH_TOKEN);

        tokenService.saveToken(userDto, accessToken, refreshToken);

        response.setUser(userDto);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(generalMessageSource.getMessage(MessageConstants.USER_WITH_USERNAME_LOGGED_IN_SUCCESSFULLY, new Object[]{userDto.getUsername()}, LocaleUtil.getLocale()));

        return response;
    }

    public UserDto findByUsername(final String username) {
        UserDto user = null;
        if (null != username) {
            User userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not FOUND!!!"));
            if (null != userEntity) {
                user = userMapper.mapToUserDto(UserDto.builder().build(), userEntity);
            }
        }
        return user;
    }
}
