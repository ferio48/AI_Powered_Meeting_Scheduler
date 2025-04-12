package com.aipowered.meeting.scheduler.service.impl;

import com.aipowered.meeting.scheduler.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EmailService} for sending reminder emails.
 * Uses {@link JavaMailSender} to dispatch plain text messages.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends a reminder email to the specified recipient.
     *
     * @param to      the recipient's email address
     * @param subject the subject of the email
     * @param text    the body of the email in plain text
     */
    @Override
    public void sendReminder(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
