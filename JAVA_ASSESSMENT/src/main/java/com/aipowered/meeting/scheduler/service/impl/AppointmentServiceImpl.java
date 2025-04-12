package com.aipowered.meeting.scheduler.service.impl;

import com.aipowered.meeting.scheduler.mapper.MeetingInformationMapper;
import com.aipowered.meeting.scheduler.model.entity.ConversationResult;
import com.aipowered.meeting.scheduler.model.entity.MeetingInformation;
import com.aipowered.meeting.scheduler.model.entity.Message;
import com.aipowered.meeting.scheduler.model.response.BasicRestResponse;
import com.aipowered.meeting.scheduler.repositories.MeetingInformationRepository;
import com.aipowered.meeting.scheduler.service.AppointmentService;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.model.request.AppointmentRequest;
import com.aipowered.meeting.scheduler.util.LoggedInUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link AppointmentService} for scheduling property viewing appointments.
 * This class interacts with Gemini AI to extract meeting details from user input
 * and stores the appointment in the database.
 */
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final GeminiClient geminiClient;
    private final MeetingInformationRepository meetingInformationRepository;
    private final MeetingInformationMapper meetingInformationMapper;

    private final List<Message> history = new ArrayList<>();
    private boolean firstChatMessage = true;

    /**
     * Handles user appointment scheduling using Gemini AI.
     * Maintains a message history for conversation context and responds with
     * follow-up questions if information is incomplete.
     *
     * @param appointmentRequest the request containing user's message
     * @return a {@link BasicRestResponse} with the assistant's reply or confirmation
     */
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

    /**
     * Adds the user message (and AI prompt if it's the first interaction) to the history.
     *
     * @param appointmentRequest user message input
     */
    private void addUserMessageToHistory(AppointmentRequest appointmentRequest) {
        String finalMessage = getInitialMessageTemplate(appointmentRequest);
        history.add(Message.builder().role("user").text(finalMessage).build());
        firstChatMessage = false;
    }

    /**
     * Handles cases where AI response lacks full meeting details and follow-up is needed.
     * Adds the assistant message to history and prepares the next prompt.
     *
     * @param response           response object to populate
     * @param appointmentRequest original user message
     * @param result             incomplete result from Gemini
     */
    private void handleIncompleteConversation(BasicRestResponse response, AppointmentRequest appointmentRequest, ConversationResult result) {
        Message assistantMessage = Message.builder()
                .role("assistant")
                .text(result.getFollowUpQuestion())
                .build();
        history.add(assistantMessage);

        response.setStatus(HttpStatus.OK.value());
        response.setMessage(assistantMessage.getText());

        history.add(Message.builder()
                .role("user")
                .text(appointmentRequest.getText())
                .build());
    }

    /**
     * Saves the fully formed meeting information returned from Gemini and assigns the current user.
     *
     * @param result the completed conversation containing all appointment info
     * @return the saved {@link MeetingInformation} entity
     */
    public MeetingInformation saveMeeting(ConversationResult result) {
        MeetingInformation meetingInformation = meetingInformationMapper
                .toMeetingInformation(MeetingInformation.builder().build(), result.getMeetingInfo());

        User currentUser = User.builder().build();
        currentUser.setId(LoggedInUserUtil.getLoggedInUserID());
        meetingInformation.setUser(currentUser);

        return meetingInformationRepository.save(meetingInformation);
    }

    /**
     * Resets the internal conversation state for future sessions.
     */
    private void resetConversationState() {
        history.clear();
        firstChatMessage = true;
    }

    /**
     * Constructs the initial system prompt for the assistant, prepending user input if it's the first message.
     *
     * @param appointmentRequest the request from the user
     * @return the full prompt string for Gemini
     */
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

    /**
     * Builds a user-friendly confirmation message once the meeting is saved.
     *
     * @param meetingInformation the saved meeting
     * @return a summary string for the user
     */
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

