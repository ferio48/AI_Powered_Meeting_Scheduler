package com.gemini.aichatbot.repositories;

import com.gemini.aichatbot.model.entity.MeetingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository interface for managing {@link MeetingInformation} entities.
 *
 * Extends {@link JpaRepository} to provide CRUD operations and custom queries
 * related to appointment scheduling and reminders.
 */
@Repository
public interface MeetingInformationRepository extends JpaRepository<MeetingInformation, Integer> {

    /**
     * Finds all meetings that are scheduled between a given date-time range and
     * have not yet been sent a reminder.
     *
     * This is useful for scheduling 1-hour prior reminders for upcoming appointments.
     * It combines {@code appointmentDate} and {@code appointmentTime} into a timestamp
     * and compares it with the provided range.
     *
     * @param from the lower bound of the timestamp range
     * @param to   the upper bound of the timestamp range
     * @return a list of {@link MeetingInformation} entries matching the criteria
     */
    @Query("""
        SELECT m FROM MeetingInformation m
        WHERE FUNCTION('TIMESTAMP', m.appointmentDate, m.appointmentTime)
        BETWEEN :from AND :to
        AND m.reminderSent = false
    """)
    List<MeetingInformation> findMeetingsToRemind(
            @Param("from") Date from,
            @Param("to") Date to
    );
}
