package com.java.assessment.JAVA_ASSESSMENT.security.model;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Class representing an Authentication Response containing token, user details, and status.
 * This class is used to encapsulate the response data after successful authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 550269063035507976L;

    /**
     * The authentication token generated after successful authentication.
     */
    private String accessToken;

    private String refreshToken;

    /**
     * The user details associated with the authenticated user.
     */
    private UserDto user;

    private Integer status;

    private String message;

    private List<? extends Serializable> errorList;
}
