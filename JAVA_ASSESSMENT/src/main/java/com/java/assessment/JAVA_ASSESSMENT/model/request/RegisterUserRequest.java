package com.java.assessment.JAVA_ASSESSMENT.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 453657324234L;

    @NotNull(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Email cannot be blank")
    private String emailAddress;

    @NotNull(message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotNull(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Confirm Password cannot be blank")
    private String confirmPassword;
}
