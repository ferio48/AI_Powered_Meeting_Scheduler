package com.java.assessment.JAVA_ASSESSMENT.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 26426290547229735L;

    private String accessToken;

    private String refreshToken;

    private Date accessTokenExpireAt;

    private Date refreshTokenExpireAt;

    private UserDto userDto;

}
