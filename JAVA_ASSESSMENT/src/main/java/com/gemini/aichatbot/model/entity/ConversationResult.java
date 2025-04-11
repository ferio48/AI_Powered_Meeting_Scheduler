package com.gemini.aichatbot.model.entity;

import com.gemini.aichatbot.model.dto.MeetingInformationDto;
import lombok.*;

/**
 * Wrapper class representing the result of a conversation with the AI assistant.
 *
 * This class either contains a fully extracted {@link MeetingInformationDto}
 * when all required information is available, or a follow-up question prompting
 * the user to provide missing information.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResult {

    /**
     * The meeting information extracted from the user's message,
     * when all required fields are present.
     */
    private MeetingInformationDto meetingInfo;

    /**
     * A follow-up question asking for missing or unclear fields,
     * returned when the meeting info is incomplete.
     */
    private String followUpQuestion;

    /**
     * Factory method to construct a complete {@link ConversationResult}
     * from the extracted {@link MeetingInformationDto}.
     *
     * @param dto the meeting information extracted from the AI
     * @return a {@code ConversationResult} object containing meeting info
     */
    public static ConversationResult fromJson(MeetingInformationDto dto) {
        ConversationResult result = new ConversationResult();
        result.meetingInfo = dto;
        return result;
    }

    /**
     * Factory method to construct a {@link ConversationResult} containing
     * a follow-up question when the AI still requires additional input.
     *
     * @param question the AI-generated follow-up question
     * @return a {@code ConversationResult} with the follow-up question
     */
    public static ConversationResult fromQuestion(String question) {
        ConversationResult result = new ConversationResult();
        result.followUpQuestion = question;
        return result;
    }

    /**
     * Checks whether the conversation result contains all necessary meeting details.
     *
     * @return {@code true} if the meeting information is complete, otherwise {@code false}
     */
    public boolean isComplete() {
        return meetingInfo != null;
    }
}
