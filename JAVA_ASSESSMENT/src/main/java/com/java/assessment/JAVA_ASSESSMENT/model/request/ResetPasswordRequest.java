package com.java.assessment.JAVA_ASSESSMENT.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotNull(message = "Old Password cannot be blank")
    private String oldPassword;

    @NotNull(message = "New Password cannot be blank")
    private String newPassword;

    @NotNull(message = "Confirm Password cannot be blank")
    private String confirmPassword;
}
