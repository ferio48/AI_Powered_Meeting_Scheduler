package com.gemini.aichatbot.service.impl;

import com.gemini.aichatbot.mapper.MeetingInformationMapper;
import com.gemini.aichatbot.model.entity.ConversationResult;
import com.gemini.aichatbot.model.entity.MeetingInformation;
import com.gemini.aichatbot.model.entity.Message;
import com.gemini.aichatbot.model.response.BasicRestResponse;
import com.gemini.aichatbot.repositories.MeetingInformationRepository;
import com.gemini.aichatbot.service.AppointmentService;
import com.gemini.aichatbot.model.entity.User;
import com.gemini.aichatbot.model.request.AppointmentRequest;
import com.gemini.aichatbot.util.LoggedInUserUtil;
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

    private final List<Message> history = new ArrayList<>();
    private boolean firstChatMessage = true;

    @Override
    public BasicRestResponse schedule(AppointmentRequest appointmentRequest) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        try {
            addUserMessageToHistory(appointmentRequest);

            ConversationResult result = geminiClient.handleConversation(history);

            while (!result.isComplete()) {
                handleIncompleteConversation(response, appointmentRequest, result);
                return response;
            }

            // Save appointment and reset conversation
            MeetingInformation savedMeeting = saveMeeting(result);
            resetConversationState();

            response.setStatus(HttpStatus.OK.value());
            response.setMessage(buildConfirmation(savedMeeting));
        } catch (Exception ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error occurred during scheduling a meeting...");
        }

        return response;
    }

    private void addUserMessageToHistory(AppointmentRequest appointmentRequest) {
        String finalMessage = getInitialMessageTemplate(appointmentRequest);
        history.add(Message.builder().role("user").text(finalMessage).build());
        firstChatMessage = false;
    }

    private void handleIncompleteConversation(BasicRestResponse response, AppointmentRequest appointmentRequest, ConversationResult result) {
        Message assistantMessage = Message.builder()
                .role("assistant")
                .text(result.getFollowUpQuestion())
                .build();
        history.add(assistantMessage);

        response.setStatus(HttpStatus.OK.value());
        response.setMessage(assistantMessage.getText());

        // Optional: add user reply (if looping from UI)
        history.add(Message.builder()
                .role("user")
                .text(appointmentRequest.getText())
                .build());
    }

    public MeetingInformation saveMeeting(ConversationResult result) {
        MeetingInformation meetingInformation = meetingInformationMapper
                .toMeetingInformation(MeetingInformation.builder().build(), result.getMeetingInfo());

        User currentUser = User.builder().build();
        currentUser.setId(LoggedInUserUtil.getLoggedInUserID());
        meetingInformation.setUser(currentUser);

        return meetingInformationRepository.save(meetingInformation);
    }

    private void resetConversationState() {
        history.clear();
        firstChatMessage = true;
    }

    private String getInitialMessageTemplate(AppointmentRequest appointmentRequest) {
        String systemPrompt = """
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

                Once all required fields are collected, return only the extracted values in proper JSON format.
                """;

        return firstChatMessage
                ? systemPrompt + "\nUser message: " + appointmentRequest.getText()
                : appointmentRequest.getText();
    }

    public String buildConfirmation(MeetingInformation meetingInformation) {
        return String.format(
                "Your appointment has been scheduled! %s will meet %s at %s on %s at %s.",
                meetingInformation.getLandlordName(),
                meetingInformation.getTenantName(),
                meetingInformation.getPropertyAddress(),
                meetingInformation.getAppointmentDate(),
                meetingInformation.getAppointmentTime()
        );
    }
}

