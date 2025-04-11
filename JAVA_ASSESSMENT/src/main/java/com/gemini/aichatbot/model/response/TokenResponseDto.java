package com.gemini.aichatbot.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data Transfer Object representing token details for a user.
 *
 * Typically used in API responses after authentication or token refresh
 * to return access and refresh tokens along with their expiry details and user info.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    /**
     * Unique identifier of the token record.
     */
    private Integer id;

    /**
     * The JWT access token issued to the user.
     */
    private String accessToken;

    /**
     * The JWT refresh token used to obtain a new access token.
     */
    private String refreshToken;

    /**
     * Expiration timestamp of the access token.
     */
    private Date accessTokenExpireAt;

    /**
     * Expiration timestamp of the refresh token.
     */
    private Date refreshTokenExpireAt;

    /**
     * Associated user information for whom the token was issued.
     */
    private UserResponseDto userResponseDto;
}
