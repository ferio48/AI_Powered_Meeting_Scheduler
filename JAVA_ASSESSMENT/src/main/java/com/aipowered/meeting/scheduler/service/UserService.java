package com.aipowered.meeting.scheduler.service;

import com.aipowered.meeting.scheduler.exception.ResourceNotFoundException;
import com.aipowered.meeting.scheduler.model.request.RegisterUserRequest;
import com.aipowered.meeting.scheduler.security.model.AuthenticationResponse;
import com.aipowered.meeting.scheduler.model.dto.UserDto;

/**
 * Service interface for user-related operations.
 *
 * Defines methods for retrieving user information and registering new users.
 */
public interface UserService {

    /**
     * Retrieves user details based on the provided username.
     *
     * @param userName the username of the user to look up
     * @return {@link UserDto} containing user information
     * @throws ResourceNotFoundException
     *         if the user is not found
     */
    UserDto findByUsername(final String userName);

    /**
     * Registers a new user using email and password.
     *
     * This method performs:
     * - Email uniqueness validation
     * - Password decoding and validation (Base64 encoded)
     * - User creation and persistence
     * - Authentication response generation with a JWT token
     *
     * @param registerUserRequest the registration request containing user details
     * @return {@link AuthenticationResponse} with token, message, and status
     */
    AuthenticationResponse registerWithEmail(final RegisterUserRequest registerUserRequest);
}
