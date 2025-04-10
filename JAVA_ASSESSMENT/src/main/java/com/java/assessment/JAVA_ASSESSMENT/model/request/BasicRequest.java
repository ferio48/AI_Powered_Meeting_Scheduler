package com.java.assessment.JAVA_ASSESSMENT.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicRequest {

    private Integer pageNumber;

    private Integer pageSize;

    private String sortBy;

    private String sortDir;

}
