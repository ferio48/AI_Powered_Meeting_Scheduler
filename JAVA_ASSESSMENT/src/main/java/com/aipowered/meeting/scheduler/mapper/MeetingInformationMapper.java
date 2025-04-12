package com.aipowered.meeting.scheduler.mapper;

import com.aipowered.meeting.scheduler.model.entity.MeetingInformation;
import com.aipowered.meeting.scheduler.model.dto.MeetingInformationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between {@link MeetingInformation} entity
 * and {@link MeetingInformationDto} using MapStruct.
 *
 * Configured to ignore null values when mapping to avoid overwriting existing fields with nulls.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MeetingInformationMapper {

    /**
     * Maps fields from a {@link MeetingInformationDto} to an existing {@link MeetingInformation} entity.
     * Only non-null fields from the DTO are used to update the target.
     *
     * @param meetingInformation the existing target entity to update
     * @param meetingInformationDto the source DTO containing new data
     * @return the updated {@link MeetingInformation} entity
     */
    MeetingInformation toMeetingInformation(
            @MappingTarget MeetingInformation meetingInformation,
            MeetingInformationDto meetingInformationDto
    );

    /**
     * Maps fields from a {@link MeetingInformation} entity to an existing {@link MeetingInformationDto}.
     * Only non-null fields from the entity are copied to the DTO.
     *
     * @param meetingInformationDto the existing target DTO to update
     * @param meetingInformation the source entity
     * @return the updated {@link MeetingInformationDto}
     */
    MeetingInformationDto toMeetingInformationDto(
            @MappingTarget MeetingInformationDto meetingInformationDto,
            MeetingInformation meetingInformation
    );
}
