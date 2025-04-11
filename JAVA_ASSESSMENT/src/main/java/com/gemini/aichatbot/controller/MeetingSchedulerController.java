package com.gemini.aichatbot.controller;

import com.gemini.aichatbot.model.response.BasicRestResponse;
import com.gemini.aichatbot.constant.Authority;
import com.gemini.aichatbot.model.request.AppointmentRequest;
import com.gemini.aichatbot.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST controller for handling meeting scheduling requests via AI assistance.
 *
 * Exposes an endpoint to initiate or continue a conversation with the AI chatbot
 * for scheduling property viewing appointments.
 */
@RestController
@Validated
@RequestMapping("/meeting")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class MeetingSchedulerController {

    private final AppointmentService appointmentService;

    /**
     * Endpoint to schedule a property viewing appointment.
     *
     * Accepts a message from the user, processes it through the AI assistant (Gemini),
     * and responds with either a follow-up question or a confirmation once all details are collected.
     *
     * @param appointmentRequest the message or input text from the user
     * @return a {@link ResponseEntity} containing a {@link BasicRestResponse}
     */
    @PostMapping("/schedule")
    @PreAuthorize(Authority.USER_CREATE)
    public ResponseEntity<BasicRestResponse> scheduleAppointment(
            final @Valid @RequestBody AppointmentRequest appointmentRequest
    ) {
        return ResponseEntity.ok(appointmentService.schedule(appointmentRequest));
    }
}
