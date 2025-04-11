package com.gemini.aichatbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of user permissions used for authorization control.
 *
 * Each permission corresponds to a specific action that can be granted to a user role.
 * These permissions can be used in security annotations or access control checks.
 */
@RequiredArgsConstructor
public enum Permission {

    /**
     * Permission to view/read user details.
     */
    USER_READ("user:read"),

    /**
     * Permission to update/edit user information.
     */
    USER_UPDATE("user:update"),

    /**
     * Permission to create new users.
     */
    USER_CREATE("user:create"),

    /**
     * Permission to delete users.
     */
    USER_DELETE("user:delete");

    /**
     * The permission string used in security checks or authority mappings.
     */
    @Getter
    private final String permission;
}
