package com.java.assessment.JAVA_ASSESSMENT.model.dto;

import com.java.assessment.JAVA_ASSESSMENT.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 26426290547223598L;

    private String name;

    private String username;

    private String emailAddress;

    private String password;

    private String phoneNumber;

    private Role role;

    private List<TokenDto> tokenDtoList;

}
