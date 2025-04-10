package com.java.assessment.JAVA_ASSESSMENT.security.service.impl;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.Token;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.repositories.TokenRepository;
import com.java.assessment.JAVA_ASSESSMENT.security.config.JwtUtil;
import com.java.assessment.JAVA_ASSESSMENT.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void saveToken(UserDto userDto, String accessToken, String refreshToken) {
        try {
            if(null != userDto && StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
                final User user = User.builder().build();
                user.setId(userDto.getId());

                final Date accessTokenExpireTime = jwtUtil.getExpireTimeFromToken(accessToken);
                final Date refreshTokenExpireTime = jwtUtil.getExpireTimeFromToken(refreshToken);

                final Token tokenEntity = Token
                        .builder()
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
