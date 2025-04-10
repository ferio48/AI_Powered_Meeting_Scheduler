package com.java.assessment.JAVA_ASSESSMENT.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    private Integer id;

    private String accessToken;

    private String refreshToken;

    private Date accessTokenExpireAt;

    private Date refreshTokenExpireAt;

    private UserResponseDto userResponseDto;

}
