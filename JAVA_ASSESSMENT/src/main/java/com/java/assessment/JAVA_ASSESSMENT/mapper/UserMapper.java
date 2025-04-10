package com.java.assessment.JAVA_ASSESSMENT.mapper;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "tokenDtoList", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDto mapToUserDto(@MappingTarget UserDto userDto, User user);

}
