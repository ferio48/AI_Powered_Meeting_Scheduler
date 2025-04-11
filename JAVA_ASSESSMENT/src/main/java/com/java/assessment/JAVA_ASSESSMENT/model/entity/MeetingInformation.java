package com.java.assessment.JAVA_ASSESSMENT.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "meeting_info")
public class MeetingInformation extends Base{

    @Column(name = "appointment_date")
    private Date appointmentDate;

    @Column(name = "appointment_time")
    private Time appointmentTime;

    @Column(name = "property_address")
    private String propertyAddress;

    @Column(name = "landlord_name")
    private String landlordName;

    @Column(name = "landlord_contact")
    private String landlordContact;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "tenant_contact")
    private String tenantContact;

    @Column(name = "reminder_sent")
    private boolean reminderSent = false;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
