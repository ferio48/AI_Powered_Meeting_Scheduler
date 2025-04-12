package com.aipowered.meeting.scheduler.service;

import com.aipowered.meeting.scheduler.mapper.MeetingInformationMapper;
import com.aipowered.meeting.scheduler.model.dto.MeetingInformationDto;
import com.aipowered.meeting.scheduler.model.entity.ConversationResult;
import com.aipowered.meeting.scheduler.model.entity.MeetingInformation;
import com.aipowered.meeting.scheduler.model.request.AppointmentRequest;
import com.aipowered.meeting.scheduler.model.response.BasicRestResponse;
import com.aipowered.meeting.scheduler.repositories.MeetingInformationRepository;
import com.aipowered.meeting.scheduler.security.model.AuthenticationDetails;
import com.aipowered.meeting.scheduler.service.impl.AppointmentServiceImpl;
import com.aipowered.meeting.scheduler.service.impl.GeminiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class AppointmentServiceTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private GeminiClient geminiClient;

    @Mock
    private MeetingInformationRepository meetingInformationRepository;

    @Mock
    private MeetingInformationMapper meetingInformationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthenticationDetails authDetails = new AuthenticationDetails();
        authDetails.setLoggedInUserId(1); // simulate user ID
        authDetails.setLoggedInUsername("testuser");
        authDetails.setLoggedInUserRole("USER");

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("testuser", null, List.of());
        authentication.setDetails(authDetails);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

     @Test
     void testScheduleAppointment_whenConversationIsComplete_shouldSaveMeeting() throws Exception {
         AppointmentRequest request = new AppointmentRequest("Schedule a meeting on April 15 at 3 PM");

         MeetingInformationDto dto = MeetingInformationDto.builder()
                 .appointmentDate(new java.sql.Date(System.currentTimeMillis()))
                 .appointmentTime(new java.sql.Time(System.currentTimeMillis()))
                 .propertyAddress("123 Main St")
                 .landlordName("Alice")
                 .landlordContact("+1234567890")
                 .tenantName("Bob")
                 .tenantContact("+0987654321")
                 .build();

         ConversationResult conversationResult = ConversationResult.fromJson(dto);
         when(geminiClient.handleConversation(any())).thenReturn(conversationResult);

         MeetingInformation mockEntity = new MeetingInformation();
         mockEntity.setLandlordName("Alice");
         mockEntity.setTenantName("Bob");
         mockEntity.setPropertyAddress("123 Main St");
         mockEntity.setAppointmentDate(dto.getAppointmentDate());
         mockEntity.setAppointmentTime(dto.getAppointmentTime());

         when(meetingInformationMapper.toMeetingInformation(any(), any())).thenReturn(mockEntity);
         when(meetingInformationRepository.save(any())).thenReturn(mockEntity);
         when(appointmentService.saveMeeting(conversationResult)).thenReturn(mockEntity);

         BasicRestResponse response = appointmentService.schedule(request);

         assertEquals(200, response.getStatus());
         assertTrue(response.getMessage().contains("Your appointment has been scheduled"));
     }

    @Test
    void testScheduleAppointment_whenConversationIsIncomplete_shouldReturnFollowUp() throws Exception {
        AppointmentRequest request = new AppointmentRequest("Schedule a meeting");

        ConversationResult incompleteResult = ConversationResult.fromQuestion("What is the time of the meeting?");
        when(geminiClient.handleConversation(any())).thenReturn(incompleteResult);

        BasicRestResponse response = appointmentService.schedule(request);

        assertEquals(200, response.getStatus());
        assertEquals("What is the time of the meeting?", response.getMessage());
    }

    @Test
    void testScheduleAppointment_whenExceptionThrown_shouldHandleGracefully() throws Exception {
        AppointmentRequest request = new AppointmentRequest("bad input");

        when(geminiClient.handleConversation(any())).thenThrow(new RuntimeException("Gemini API failed"));

        BasicRestResponse response = appointmentService.schedule(request);

        assertEquals(500, response.getStatus());
        assertEquals("Error occurred during scheduling a meeting...", response.getMessage());
    }
}
