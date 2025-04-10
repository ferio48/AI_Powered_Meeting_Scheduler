package com.java.assessment.JAVA_ASSESSMENT.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest implements Serializable {

    private static final long serialVersionUID = 7683243289L;

    private List<String> toList;

    private List<String> ccList;

    private List<String> bccList;

    private String subject;

    private String body;

    private String attachment;

}
