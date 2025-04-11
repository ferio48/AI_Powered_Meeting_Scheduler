package com.java.assessment.JAVA_ASSESSMENT.mapper;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.MeetingInformationDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.MeetingInformation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MeetingInformationMapper {

    MeetingInformation toMeetingInformation(@MappingTarget MeetingInformation meetingInformation, MeetingInformationDto meetingInformationDto);

    MeetingInformationDto toMeetingInformationDto(@MappingTarget MeetingInformationDto meetingInformationDto, MeetingInformation meetingInformation);

}
