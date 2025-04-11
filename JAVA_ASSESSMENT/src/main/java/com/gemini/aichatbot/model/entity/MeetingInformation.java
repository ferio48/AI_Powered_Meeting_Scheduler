package com.gemini.aichatbot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

/**
 * Entity representing a scheduled property viewing meeting.
 *
 * Stores essential meeting details such as date, time, participants,
 * location, and notification status.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "meeting_info")
public class MeetingInformation extends Base {

    /**
     * The date of the scheduled appointment.
     */
    @Column(name = "appointment_date")
    private Date appointmentDate;

    /**
     * The time of the scheduled appointment.
     */
    @Column(name = "appointment_time")
    private Time appointmentTime;

    /**
     * The property address where the meeting will take place.
     */
    @Column(name = "property_address")
    private String propertyAddress;

    /**
     * The full name of the landlord attending the appointment.
     */
    @Column(name = "landlord_name")
    private String landlordName;

    /**
     * The contact details (email or phone) of the landlord.
     */
    @Column(name = "landlord_contact")
    private String landlordContact;

    /**
     * The full name of the tenant attending the appointment.
     */
    @Column(name = "tenant_name")
    private String tenantName;

    /**
     * The contact details (email or phone) of the tenant.
     */
    @Column(name = "tenant_contact")
    private String tenantContact;

    /**
     * Indicates whether the reminder notification has been sent for this meeting.
     * Default value is {@code false}.
     */
    @Column(name = "reminder_sent")
    private boolean reminderSent = false;

    /**
     * The user (creator/owner) associated with this meeting.
     * Typically the logged-in user who scheduled it.
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}