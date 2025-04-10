package com.java.assessment.JAVA_ASSESSMENT.security.service;


import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;

public interface TokenService {
    void saveToken(UserDto userDto, String accessToken, String refreshToken);
}
