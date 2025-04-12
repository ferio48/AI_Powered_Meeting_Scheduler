package com.aipowered.meeting.scheduler.model.dto;

import com.aipowered.meeting.scheduler.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user in the system.
 *
 * Used to transfer user-related data between layers without exposing the entity directly.
 * Typically used in authentication, registration, profile views, and token operations.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 26426290547223598L;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * Unique username used for login or display.
     */
    private String username;

    /**
     * Email address of the user.
     */
    private String emailAddress;

    /**
     * Password of the user (usually encrypted).
     */
    private String password;

    /**
     * Phone number of the user.
     */
    private String phoneNumber;

    /**
     * Role assigned to the user (e.g., USER, ADMIN).
     */
    private Role role;

    /**
     * List of tokens (access + refresh) issued to the user.
     */
    private List<TokenDto> tokenDtoList;
}
