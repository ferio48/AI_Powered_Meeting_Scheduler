package com.java.assessment.JAVA_ASSESSMENT.service.impl;

import com.java.assessment.JAVA_ASSESSMENT.mapper.MeetingInformationMapper;
import com.java.assessment.JAVA_ASSESSMENT.model.dto.MeetingInformationDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.ConversationResult;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.MeetingInformation;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.Message;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.model.request.AppointmentRequest;
import com.java.assessment.JAVA_ASSESSMENT.model.response.BasicRestResponse;
import com.java.assessment.JAVA_ASSESSMENT.repositories.MeetingInformationRepository;
import com.java.assessment.JAVA_ASSESSMENT.service.AppointmentService;
import com.java.assessment.JAVA_ASSESSMENT.util.LoggedInUserUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final GeminiClient geminiClient;

    private final MeetingInformationRepository meetingInformationRepository;

    private final MeetingInformationMapper meetingInformationMapper;

    List<Message> history = new ArrayList<>();

    private boolean firstChatMessage = true;

    @Override
    public BasicRestResponse schedule(AppointmentRequest appointmentRequest) {
        BasicRestResponse basicRestResponse = BasicRestResponse.builder().build();
        try {

            String finalMessage = getInitialMessageTemplate(appointmentRequest);

            firstChatMessage = false;

            Message message = Message.builder().role("user").text(finalMessage).build();
            history.add(message);

            ConversationResult conversationResult = geminiClient.handleConversation(history);
            while (!conversationResult.isComplete()) {
                Message messageFromAssistant = Message.builder().role("assistant").text(conversationResult.getFollowUpQuestion()).build();
                history.add(messageFromAssistant);

                if (!conversationResult.isComplete()) {
                    basicRestResponse.setStatus(HttpStatus.OK.value());
                    basicRestResponse.setMessage(messageFromAssistant.getText());
                    return basicRestResponse;
                }

                Message messageFromUser = Message.builder().role("user").text(appointmentRequest.getText()).build();
                history.add(messageFromUser);

                conversationResult = geminiClient.handleConversation(history);
            }

            history.clear();
            firstChatMessage = true;
            MeetingInformation meetingInformation = meetingInformationMapper.toMeetingInformation(MeetingInformation.builder().build(), conversationResult.getMeetingInfo());
            User currentUser = User.builder().build();
            currentUser.setId(LoggedInUserUtil.getLoggedInUserID());
            meetingInformation.setUser(currentUser);
            meetingInformationRepository.save(meetingInformation);

            basicRestResponse.setStatus(HttpStatus.OK.value());
            basicRestResponse.setMessage(buildConfirmation(meetingInformation));
        } catch (Exception ex) {
            basicRestResponse.setStatus(HttpStatus.OK.value());
            basicRestResponse.setMessage("Error occurred during scheduling a meeting...");
        }

        return basicRestResponse;
    }

    private String getInitialMessageTemplate(AppointmentRequest appointmentRequest) {
        String initialMessage = """
                You are a helpful and friendly AI assistant that helps schedule property viewing appointments.

                Your task is to extract the following information from user messages:
                - appointmentDate (YYYY-MM-DD)
                - appointmentTime (HH:mm:ss)
                - propertyAddress
                - landlordName
                - landlordContact
                - tenantName
                - tenantContact

                If a user provides date/time in natural language (e.g., "next Tuesday", "3pm"), convert it to standard format.

                If any information is missing, respond in a polite and conversational tone asking only for the missing part.
                
                If user tries to divert from scheduling meeting please take him back to scheduling a meeting.
                
                Respond with a polite message if asked something which is not related to scheduling meeting.

                Avoid listing the entire template or asking for all details again. Be concise and natural.

                Once all required fields are collected, return the extracted values in proper JSON format.
                """;


        String finalMessage;
        if (firstChatMessage) finalMessage = initialMessage + "\nUser message: " + appointmentRequest.getText();
        else finalMessage = appointmentRequest.getText();
        return finalMessage;
    }

    public String buildConfirmation(MeetingInformation meetingInformation) {
        return String.format(
                "Your appointment has been scheduled! %s will meet %s at %s on %s at %s.",
                meetingInformation.getLandlordName(),
                meetingInformation.getTenantName(),
                meetingInformation.getPropertyAddress(),
                meetingInformation.getAppointmentDate().toString(),
                meetingInformation.getAppointmentTime().toString()
        );
    }
}
