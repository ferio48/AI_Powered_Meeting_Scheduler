package com.gemini.aichatbot.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * Data Transfer Object used for registering a new user.
 *
 * Contains all required fields for user registration including name, email,
 * phone number, and password information. Validation annotations ensure
 * mandatory fields are provided before processing.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 453657324234L;

    /**
     * Full name of the user.
     */
    @NotNull(message = "Name cannot be blank")
    private String name;

    /**
     * Email address of the user. Used as a primary contact and login identifier.
     */
    @NotNull(message = "Email cannot be blank")
    private String emailAddress;

    /**
     * Phone number of the user. May be used for verification or login.
     */
    @NotNull(message = "Phone Number cannot be blank")
    private String phoneNumber;

    /**
     * Base64-encoded password provided by the user during registration.
     */
    @NotNull(message = "Password cannot be blank")
    private String password;

    /**
     * Base64-encoded confirmation of the password.
     * Must match the password field to be valid.
     */
    @NotNull(message = "Confirm Password cannot be blank")
    private String confirmPassword;
}
