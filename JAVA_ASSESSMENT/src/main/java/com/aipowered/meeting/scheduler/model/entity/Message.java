package com.aipowered.meeting.scheduler.model.entity;

import lombok.*;

/**
 * Represents a single message exchanged in a conversation with the AI assistant.
 *
 * This class is used to maintain the conversation history by capturing
 * both user and assistant messages, including their role and content.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    /**
     * The role of the message sender.
     * Expected values: {@code "user"} or {@code "assistant"}.
     */
    private String role;

    /**
     * The textual content of the message.
     */
    private String text;
}
