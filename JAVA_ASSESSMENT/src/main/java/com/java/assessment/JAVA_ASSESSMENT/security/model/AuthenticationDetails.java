package com.java.assessment.JAVA_ASSESSMENT.security.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDetails implements Serializable {

    private static final long serialVersionUID = 550269061132507976L;

    private Integer loggedInUserId;

    private String loggedInUsername;

    private String loggedInUserRole;

}
