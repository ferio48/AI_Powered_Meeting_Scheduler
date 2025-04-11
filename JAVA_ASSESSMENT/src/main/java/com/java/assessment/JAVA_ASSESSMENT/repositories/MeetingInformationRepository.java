package com.java.assessment.JAVA_ASSESSMENT.repositories;

import com.java.assessment.JAVA_ASSESSMENT.model.entity.MeetingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MeetingInformationRepository extends JpaRepository<MeetingInformation, Integer> {

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
