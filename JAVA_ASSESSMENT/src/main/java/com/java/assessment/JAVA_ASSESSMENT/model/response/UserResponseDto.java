package com.java.assessment.JAVA_ASSESSMENT.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Integer id;

    private String name;

    private String username;

    private String emailAddress;

    private String password;

    private String phoneNumber;

    private RoleResponseDto role;

    private List<TokenResponseDto> tokenList;

}
