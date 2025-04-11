package com.java.assessment.JAVA_ASSESSMENT.model.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private String role;
    private String text;
}
