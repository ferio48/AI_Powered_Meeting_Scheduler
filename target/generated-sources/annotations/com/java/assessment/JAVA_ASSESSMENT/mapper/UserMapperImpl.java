package com.java.assessment.JAVA_ASSESSMENT.mapper;

import com.java.assessment.JAVA_ASSESSMENT.model.dto.UserDto;
import com.java.assessment.JAVA_ASSESSMENT.model.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-10T12:53:54+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapToUserDto(UserDto userDto, User user) {
        if ( user == null ) {
            return userDto;
        }

        if ( user.getId() != null ) {
            userDto.setId( user.getId() );
        }
        if ( user.getCreationDate() != null ) {
            userDto.setCreationDate( user.getCreationDate() );
        }
        if ( user.getCreatedBy() != null ) {
            userDto.setCreatedBy( user.getCreatedBy() );
        }
        if ( user.getLastModifiedDate() != null ) {
            userDto.setLastModifiedDate( user.getLastModifiedDate() );
        }
        if ( user.getLastModifiedBy() != null ) {
            userDto.setLastModifiedBy( user.getLastModifiedBy() );
        }
        if ( user.getName() != null ) {
            userDto.setName( user.getName() );
        }
        if ( user.getUsername() != null ) {
            userDto.setUsername( user.getUsername() );
        }
        if ( user.getEmailAddress() != null ) {
            userDto.setEmailAddress( user.getEmailAddress() );
        }
        if ( user.getPhoneNumber() != null ) {
            userDto.setPhoneNumber( user.getPhoneNumber() );
        }
        if ( user.getRole() != null ) {
            userDto.setRole( user.getRole() );
        }

        return userDto;
    }
}
