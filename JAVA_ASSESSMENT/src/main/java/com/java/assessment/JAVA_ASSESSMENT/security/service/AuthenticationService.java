package com.java.assessment.JAVA_ASSESSMENT.security.service;


import com.java.assessment.JAVA_ASSESSMENT.security.model.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse generateAuthenticationResponse(final String username, final AuthenticationResponse authenticationResponse);

}
