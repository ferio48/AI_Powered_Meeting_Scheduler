package com.aipowered.meeting.scheduler.service.impl;

import com.aipowered.meeting.scheduler.model.entity.MeetingInformation;
import com.aipowered.meeting.scheduler.model.entity.User;
import com.aipowered.meeting.scheduler.repositories.MeetingInformationRepository;
import com.aipowered.meeting.scheduler.repositories.UserRepository;
import com.aipowered.meeting.scheduler.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Scheduled component responsible for sending email reminders
 * for property viewing meetings one hour before the scheduled time.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final MeetingInformationRepository meetingInformationRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    /**
     * Scheduled task that runs every minute to check for
     * appointments starting in the next hour and sends email reminders.
     */
    @Scheduled(cron = "0 * * * * *")
    public void sendReminders() {
        Date from = toDate(LocalDateTime.now());
        Date to = toDate(LocalDateTime.now().plusHours(1));

        List<MeetingInformation> upcomingAppointments = meetingInformationRepository.findMeetingsToRemind(from, to);

        for (MeetingInformation meeting : upcomingAppointments) {
            sendReminderForMeeting(meeting);
        }
    }

    /**
     * Sends a reminder email for a given meeting, and marks it as reminded.
     *
     * @param meeting the meeting information
     */
    private void sendReminderForMeeting(MeetingInformation meeting) {
        Optional<User> optionalUser = userRepository.findById(meeting.getUser().getId());
        if (optionalUser.isEmpty()) {
            log.warn("Skipping reminder — user not found for meeting ID {}", meeting.getId());
            return;
        }

        String subject = buildEmailSubject();
        String body = buildEmailBody(meeting);
        String recipient = optionalUser.get().getEmailAddress();

        emailService.sendReminder(recipient, subject, body);
        markReminderAsSent(meeting);

        log.info("Reminder email sent to {} for meeting at {}", recipient, meeting.getAppointmentTime());
    }

    /**
     * Converts a LocalDateTime to a java.util.Date using the system default time zone.
     *
     * @param dateTime LocalDateTime to convert
     * @return converted Date object
     */
    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns the subject line for the email reminder.
     */
    private String buildEmailSubject() {
        return "🔔 Meeting Reminder: Your appointment is in 1 hour";
    }

    /**
     * Builds the email message body for the meeting reminder.
     *
     * @param meeting the meeting to include in the message
     * @return the email body text
     */
    private String buildEmailBody(MeetingInformation meeting) {
        return String.format("""
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
    }

    /**
     * Marks a meeting as reminded and persists the change.
     *
     * @param meeting the meeting entity to update
     */
    private void markReminderAsSent(MeetingInformation meeting) {
        meeting.setReminderSent(true);
        meetingInformationRepository.save(meeting);
    }
}