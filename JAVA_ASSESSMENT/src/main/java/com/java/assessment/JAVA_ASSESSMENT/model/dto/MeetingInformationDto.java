package com.java.assessment.JAVA_ASSESSMENT.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingInformationDto extends BaseDto{

    private Date appointmentDate;

    private Time appointmentTime;

    private String propertyAddress;

    private String landlordName;

    private String landlordContact;

    private String tenantName;

    private String tenantContact;

}
