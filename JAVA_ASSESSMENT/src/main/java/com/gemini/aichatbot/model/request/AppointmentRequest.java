package com.gemini.aichatbot.model.request;

import lombok.*;

/**
 * Request payload used to initiate or continue a conversation with the AI assistant
 * for scheduling property viewing appointments.
 *
 * This class typically holds the user's input message, which may contain
 * appointment details or replies to follow-up questions.
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequest {

    /**
     * The raw text message input from the user.
     * This message is analyzed by the AI assistant to extract meeting details.
     */
    private String text;
}
