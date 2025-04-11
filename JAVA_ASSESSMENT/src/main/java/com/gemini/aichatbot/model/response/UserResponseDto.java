package com.gemini.aichatbot.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object used for returning user-related information in API responses.
 *
 * Includes identity details, role, contact info, and associated tokens.
 * Typically used in responses for profile views, authentication, or admin user listings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    /**
     * Unique identifier of the user.
     */
    private Integer id;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * Unique username (may also be used for login).
     */
    private String username;

    /**
     * Email address of the user.
     */
    private String emailAddress;

    /**
     * Hashed password of the user.
     * ⚠️ This should be excluded from most API responses for security reasons.
     */
    private String password;

    /**
     * Phone number associated with the user.
     */
    private String phoneNumber;

    /**
     * Role assigned to the user.
     */
    private RoleResponseDto role;

    /**
     * List of issued tokens associated with this user.
     */
    private List<TokenResponseDto> tokenList;
}
