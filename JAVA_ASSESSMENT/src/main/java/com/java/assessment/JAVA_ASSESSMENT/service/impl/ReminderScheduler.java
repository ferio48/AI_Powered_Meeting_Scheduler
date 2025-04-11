package com.java.assessment.JAVA_ASSESSMENT.service.impl;

import com.java.assessment.JAVA_ASSESSMENT.model.entity.MeetingInformation;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import com.java.assessment.JAVA_ASSESSMENT.repositories.MeetingInformationRepository;
import com.java.assessment.JAVA_ASSESSMENT.repositories.UserRepository;
import com.java.assessment.JAVA_ASSESSMENT.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {


    private final MeetingInformationRepository meetingInformationRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 * * * * *") // Every minute
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        Date from = toDate(now);
        Date to = toDate(oneHourLater);

        List<MeetingInformation> upcomingAppointments = meetingInformationRepository
                .findMeetingsToRemind(from, to);

        for (MeetingInformation meeting : upcomingAppointments) {
            String subject = "🔔 Meeting Reminder: Your appointment is in 1 hour";
            String body = String.format("""
                Hello,

                This is a reminder that your property viewing appointment is scheduled in 1 hour.

                Address: %s
                Landlord: %s (%s)
                Tenant: %s (%s)
                Date: %s
                Time: %s

                Thank you!
                """,
                    meeting.getPropertyAddress(),
                    meeting.getLandlordName(), meeting.getLandlordContact(),
                    meeting.getTenantName(), meeting.getTenantContact(),
                    meeting.getAppointmentDate(),
                    meeting.getAppointmentTime()
            );

            Optional<User> optionalUser = userRepository.findById(meeting.getUser().getId());
            if(optionalUser.isEmpty()) continue;
            final User userToRemind = optionalUser.get();
            emailService.sendReminder(userToRemind.getEmailAddress(), subject, body);

            meeting.setReminderSent(true);
            meetingInformationRepository.save(meeting);

            log.info("Reminder email sent for meeting at {}", meeting.getAppointmentTime());
        }
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
