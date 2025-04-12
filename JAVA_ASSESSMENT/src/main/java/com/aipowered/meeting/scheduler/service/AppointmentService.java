package com.aipowered.meeting.scheduler.service;

import com.aipowered.meeting.scheduler.model.response.BasicRestResponse;
import com.aipowered.meeting.scheduler.model.request.AppointmentRequest;

/**
 * Service interface for handling appointment-related operations.
 *
 * Defines methods for scheduling property viewing appointments
 * by interacting with AI-based conversation logic and persistence layer.
 */
public interface AppointmentService {

    /**
     * Schedules a property viewing appointment based on user input.
     *
     * This method:
     * - Sends the user message to Gemini AI
     * - Iteratively collects missing fields (if any)
     * - Extracts and saves the final appointment information
     * - Returns a response with either a confirmation or a follow-up question
     *
     * @param appointmentRequest the user input/message for scheduling the appointment
     * @return a {@link BasicRestResponse} containing success message, or AI follow-up prompt
     */
    BasicRestResponse schedule(AppointmentRequest appointmentRequest);
}
