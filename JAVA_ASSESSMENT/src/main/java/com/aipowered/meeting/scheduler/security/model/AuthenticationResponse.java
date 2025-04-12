package com.aipowered.meeting.scheduler.security.model;

import com.aipowered.meeting.scheduler.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Class representing a response returned after successful or failed authentication attempts.
 *
 * Contains the generated tokens, user details, HTTP status, message,
 * and optionally a list of error messages if authentication fails.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 550269063035507976L;

    /**
     * JWT access token issued upon successful authentication.
     * This token is used for accessing protected resources.
     */
    private String accessToken;

    /**
     * JWT refresh token used to obtain a new access token after expiration.
     */
    private String refreshToken;

    /**
     * The authenticated user's details (username, role, etc.).
     */
    private UserDto user;

    /**
     * HTTP status code indicating the result of the authentication operation.
     */
    private Integer status;

    /**
     * Descriptive message about the authentication outcome.
     */
    private String message;

    /**
     * A list of errors, if any occurred during authentication or validation.
     */
    private List<? extends Serializable> errorList;
}
