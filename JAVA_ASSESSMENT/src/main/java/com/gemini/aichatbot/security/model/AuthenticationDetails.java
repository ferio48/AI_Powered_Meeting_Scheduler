package com.gemini.aichatbot.security.model;

import lombok.*;

import java.io.Serializable;

/**
 * Stores authentication-related metadata for the currently logged-in user.
 *
 * This object is typically attached to the Spring Security context during
 * the authentication process and used for retrieving user-specific details.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 550269061132507976L;

    /**
     * Unique identifier of the authenticated user.
     */
    private Integer loggedInUserId;

    /**
     * Username (typically email or phone) of the authenticated user.
     */
    private String loggedInUsername;

    /**
     * Role of the authenticated user (e.g., ADMIN, USER).
     */
    private String loggedInUserRole;
}
