package com.java.assessment.JAVA_ASSESSMENT.model.entity;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.MeetingInformationDto;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResult {

    private MeetingInformationDto meetingInfo;
    private String followUpQuestion;

    public static ConversationResult fromJson(MeetingInformationDto dto) {
        ConversationResult result = new ConversationResult();
        result.meetingInfo = dto;
        return result;
    }

    public static ConversationResult fromQuestion(String question) {
        ConversationResult result = new ConversationResult();
        result.followUpQuestion = question;
        return result;
    }

    public boolean isComplete() {
        return meetingInfo != null;
    }

}
