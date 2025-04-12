package com.aipowered.meeting.scheduler.security.service.impl;

import com.aipowered.meeting.scheduler.security.config.JwtUtil;
import com.aipowered.meeting.scheduler.security.service.AuthenticationService;
import com.aipowered.meeting.scheduler.constant.AppConstants;
import com.aipowered.meeting.scheduler.constant.MessageConstants;
import com.aipowered.meeting.scheduler.exception.ResourceNotFoundException;
import com.aipowered.meeting.scheduler.mapper.UserMapper;
import com.aipowered.meeting.scheduler.model.dto.UserDto;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.repositories.UserRepository;
import com.aipowered.meeting.scheduler.security.model.AuthenticationResponse;
import com.aipowered.meeting.scheduler.security.service.TokenService;
import com.aipowered.meeting.scheduler.util.LocaleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthenticationService} interface.
 *
 * Responsible for generating JWT tokens upon successful authentication,
 * mapping user entities to DTOs, and constructing a complete
 * {@link AuthenticationResponse}.
 */
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

    /**
     * Generates an {@link AuthenticationResponse} containing user info and JWT tokens.
     *
     * This includes:
     * - Fetching user details by username
     * - Generating access and refresh tokens
     * - Saving the tokens via {@link TokenService}
     * - Returning a fully populated authentication response
     *
     * @param username the username of the authenticated user
     * @param response the response object to populate and return
     * @return a populated {@link AuthenticationResponse}
     */
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
        response.setMessage(
                generalMessageSource.getMessage(
                        MessageConstants.USER_WITH_USERNAME_LOGGED_IN_SUCCESSFULLY,
                        new Object[]{userDto.getUsername()},
                        LocaleUtil.getLocale()
                )
        );

        return response;
    }

    /**
     * Retrieves the {@link UserDto} for the given username.
     *
     * @param username the username to search for
     * @return the corresponding {@link UserDto}
     * @throws ResourceNotFoundException if no user is found with the given username
     */
    public UserDto findByUsername(final String username) {
        UserDto user = null;
        if (username != null) {
            User userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not FOUND!!!"));
            if (userEntity != null) {
                user = userMapper.mapToUserDto(UserDto.builder().build(), userEntity);
            }
        }
        return user;
    }
}
