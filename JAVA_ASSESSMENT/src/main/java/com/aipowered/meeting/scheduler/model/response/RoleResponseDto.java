package com.aipowered.meeting.scheduler.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a user role.
 *
 * Typically used in API responses to transfer role-related information
 * without exposing the full entity or internal metadata.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {

    /**
     * The unique identifier of the role.
     */
    private Integer id;

    /**
     * The name or title of the role (e.g., "ADMIN", "USER").
     */
    private String name;
}
