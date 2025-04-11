package com.java.assessment.JAVA_ASSESSMENT.controller;

import com.java.assessment.JAVA_ASSESSMENT.constant.Authority;
import com.java.assessment.JAVA_ASSESSMENT.model.request.AppointmentRequest;
import com.java.assessment.JAVA_ASSESSMENT.model.response.BasicRestResponse;
import com.java.assessment.JAVA_ASSESSMENT.service.AppointmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/meeting")
@CrossOrigin("*")
@Slf4j
@AllArgsConstructor
public class MeetingSchedulerController {

    private final AppointmentService appointmentService;

    @PostMapping("/schedule")
    @PreAuthorize(Authority.USER_CREATE)
    public ResponseEntity<BasicRestResponse> scheduleAppointment(
            final @Valid @RequestBody AppointmentRequest appointmentRequest
            ) {
        return ResponseEntity.ok(appointmentService.schedule(appointmentRequest));
    }

}
