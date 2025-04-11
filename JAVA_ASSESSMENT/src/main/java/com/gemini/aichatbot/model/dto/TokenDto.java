package com.gemini.aichatbot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object representing token-related information for a user.
 *
 * Typically used for transferring token data between client and server
 * or internally between services during authentication or token refresh operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 26426290547229735L;

    /**
     * The JWT access token used to authorize API requests.
     */
    private String accessToken;

    /**
     * The JWT refresh token used to obtain a new access token.
     */
    private String refreshToken;

    /**
     * The expiration time of the access token.
     */
    private Date accessTokenExpireAt;

    /**
     * The expiration time of the refresh token.
     */
    private Date refreshTokenExpireAt;

    /**
     * The user associated with this token information.
     */
    private UserDto userDto;
}
