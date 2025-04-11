package com.gemini.aichatbot.service;

/**
 * Service interface for sending email notifications.
 *
 * Provides functionality to send reminders and other email messages
 * related to scheduled meetings or user activity.
 */
public interface EmailService {

    /**
     * Sends an email reminder to the specified recipient.
     *
     * @param to      the recipient's email address
     * @param subject the subject line of the email
     * @param text    the body content of the email
     */
    void sendReminder(String to, String subject, String text);
}
