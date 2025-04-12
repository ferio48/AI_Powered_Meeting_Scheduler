package com.aipowered.meeting.scheduler.security.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * Class representing an Authentication Request containing username and password.
 * This class is used to encapsulate user credentials for authentication purposes.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 550269063035507976L;

    /**
     * The username used for authentication.
     */
    @NotNull(message = "username cannot be blank")
    private String username;

    /**
     * The password used for authentication.
     */
    @NotNull(message = "password cannot be blank")
    private String password;
}
