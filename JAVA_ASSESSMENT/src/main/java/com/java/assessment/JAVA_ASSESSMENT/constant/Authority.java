package com.java.assessment.JAVA_ASSESSMENT.constant;


import com.java.assessment.JAVA_ASSESSMENT.enums.Role;

public class Authority {

    private Authority() {}

    public static final String USER_CREATE = "hasAuthority('" + AppConstants.Role.USER_CREATE + "')";

    public static final String MANAGER_CREATE = "hasAuthority('" + AppConstants.Role.MANAGER_CREATE + "')";

}
