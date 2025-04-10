package com.java.assessment.JAVA_ASSESSMENT.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {

    private Integer id;

    private String name;

}
