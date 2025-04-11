package com.gemini.aichatbot.model.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

/**
 * Data Transfer Object (DTO) for holding meeting scheduling information.
 *
 * Used to capture or return structured meeting data between the user and the system
 * without exposing the entity directly.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingInformationDto extends BaseDto {

    /**
     * The date on which the meeting is scheduled (format: YYYY-MM-DD).
     */
    private Date appointmentDate;

    /**
     * The time at which the meeting is scheduled (format: HH:mm:ss).
     */
    private Time appointmentTime;

    /**
     * The address of the property where the meeting will take place.
     */
    private String propertyAddress;

    /**
     * The full name of the landlord participating in the meeting.
     */
    private String landlordName;

    /**
     * The contact details (phone or email) of the landlord.
     */
    private String landlordContact;

    /**
     * The full name of the tenant participating in the meeting.
     */
    private String tenantName;

    /**
     * The contact details (phone or email) of the tenant.
     */
    private String tenantContact;
}
