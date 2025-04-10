package com.java.assessment.JAVA_ASSESSMENT.service;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.request.RegisterUserRequest;
import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationResponse;

public interface UserService {

    UserDto findByUsername(final String userName);

    AuthenticationResponse registerWithEmail(final RegisterUserRequest registerUserRequest);

}
