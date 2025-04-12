package com.aipowered.meeting.scheduler.security.service;


import com.aipowered.meeting.scheduler.model.dto.UserDto;

/**
 * Service interface for managing authentication tokens.
 *
 * Responsible for saving issued access and refresh tokens
 * associated with a particular user for tracking and validation purposes.
 */
public interface TokenService {

    /**
     * Persists the access and refresh tokens for the given user.
     *
     * This method is typically called after successful authentication
     * to store tokens in the database for later verification or revocation.
     *
     * @param userDto      the user for whom the tokens are issued
     * @param accessToken  the JWT access token
     * @param refreshToken the JWT refresh token
     */
    void saveToken(UserDto userDto, String accessToken, String refreshToken);
}
