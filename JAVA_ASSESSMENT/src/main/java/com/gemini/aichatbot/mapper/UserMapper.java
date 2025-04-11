package com.gemini.aichatbot.mapper;

import com.gemini.aichatbot.model.dto.UserDto;
import com.gemini.aichatbot.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for converting between {@link User} entity and {@link UserDto}.
 *
 * Uses MapStruct to perform object mapping while ignoring null properties.
 * This helps in partial updates and avoids overwriting existing values.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Maps a {@link User} entity to a {@link UserDto} object.
     * Ignores sensitive fields like password and token list for security and efficiency.
     *
     * @param userDto the existing target DTO to populate
     * @param user the source user entity to map from
     * @return the updated {@link UserDto} instance
     */
    @Mapping(target = "tokenDtoList", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDto mapToUserDto(@MappingTarget UserDto userDto, User user);
}
