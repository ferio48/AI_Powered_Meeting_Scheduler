package com.aipowered.meeting.scheduler.security.service.impl;

import com.aipowered.meeting.scheduler.model.dto.UserDto;
import com.aipowered.meeting.scheduler.model.entity.Token;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.repositories.TokenRepository;
import com.aipowered.meeting.scheduler.security.config.JwtUtil;
import com.aipowered.meeting.scheduler.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Implementation of the {@link TokenService} interface.
 *
 * Responsible for persisting access and refresh tokens associated with users.
 * It extracts expiration times from tokens and stores them along with user reference.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    /**
     * Saves the access and refresh tokens for a given user into the database.
     *
     * This method:
     * - Verifies input tokens and user
     * - Extracts expiration times from tokens
     * - Creates a {@link Token} entity and persists it via {@link TokenRepository}
     *
     * @param userDto      the user for whom the tokens were issued
     * @param accessToken  the JWT access token
     * @param refreshToken the JWT refresh token
     */
    @Override
    public void saveToken(UserDto userDto, String accessToken, String refreshToken) {
        try {
            if (userDto != null && StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
                final User user = User.builder().build();
                user.setId(userDto.getId());

                final Date accessTokenExpireTime = jwtUtil.getExpireTimeFromToken(accessToken);
                final Date refreshTokenExpireTime = jwtUtil.getExpireTimeFromToken(refreshToken);

                final Token tokenEntity = Token.builder()
                        .user(user)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .accessTokenExpireAt(accessTokenExpireTime)
                        .refreshTokenExpireAt(refreshTokenExpireTime)
                        .build();

                tokenRepository.save(tokenEntity);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}