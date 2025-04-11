package com.java.assessment.JAVA_ASSESSMENT.model.entity;

import com.java.assessment.JAVA_ASSESSMENT.enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;

    private String content;

    private String sender;

}