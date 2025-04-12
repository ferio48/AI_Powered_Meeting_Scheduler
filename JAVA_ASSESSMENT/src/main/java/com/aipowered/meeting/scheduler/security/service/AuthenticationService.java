package com.aipowered.meeting.scheduler.security.service;


import com.aipowered.meeting.scheduler.security.model.AuthenticationResponse;

/**
 * Service interface for handling authentication-related operations.
 *
 * Provides methods to generate token-based authentication responses
 * after user credentials are successfully validated.
 */
public interface AuthenticationService {

    /**
     * Generates an {@link AuthenticationResponse} for the given username.
     *
     * This includes creating a JWT access token and refresh token,
     * retrieving user details, and populating the response with status and message.
     *
     * @param username the username of the authenticated user
     * @param authenticationResponse the response object to populate and return
     * @return a fully populated {@link AuthenticationResponse} with tokens and user info
     */
    AuthenticationResponse generateAuthenticationResponse(
            final String username,
            final AuthenticationResponse authenticationResponse
    );
}
