package com.aipowered.meeting.scheduler.constant;

/**
 * Utility class containing authority expressions used in method-level security annotations.
 *
 * These constants simplify the use of {@code @PreAuthorize(...)} annotations throughout
 * the application by avoiding repeated hardcoded strings.
 *
 * Example usage:
 * <pre>
 *     {@code @PreAuthorize(Authority.USER_CREATE)}
 * </pre>
 */
public class Authority {

    private Authority() {}

    /**
     * Expression that checks if the current user has the 'user:create' authority.
     */
    public static final String USER_CREATE = "hasAuthority('" + AppConstants.Role.USER_CREATE + "')";

    /**
     * Expression that checks if the current user has the 'manager:read' authority.
     */
    public static final String MANAGER_CREATE = "hasAuthority('" + AppConstants.Role.MANAGER_CREATE + "')";
}
