package com.java.assessment.JAVA_ASSESSMENT.service;

import com.java.assessment.JAVA_ASSESSMENT.model.request.AppointmentRequest;
import com.java.assessment.JAVA_ASSESSMENT.model.response.BasicRestResponse;

public interface AppointmentService {

    BasicRestResponse schedule(AppointmentRequest appointmentRequest);

}
