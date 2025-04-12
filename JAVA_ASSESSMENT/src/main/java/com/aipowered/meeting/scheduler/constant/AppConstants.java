package com.aipowered.meeting.scheduler.constant;

/**
 * Centralized container for application-wide constants.
 *
 * This utility class contains nested static classes to organize constants
 * related to users, roles, tokens, strings, and more.
 */
public class AppConstants {

    private AppConstants() {}

    /**
     * Contains generic string-related constants.
     */
    public static class StringConstants {

        private StringConstants() {}

        /**
         * Represents an empty string.
         */
        public static final String EMPTY = "";
    }

    /**
     * Contains user-related status messages and constants.
     */
    public static class UserConstants {

        private UserConstants() {}

        /**
         * Message used after successfully saving a user.
         */
        public static final String USER_SAVED_SUCCESSFULLY = "User saved successfully!!!";
    }

    /**
     * Contains role names and permission strings used in authorization.
     */
    public static class Role {

        private Role() {}

        /**
         * Label for a regular user role.
         */
        public static final String USER = "User";

        /**
         * Authority string for permission to create users.
         */
        public static final String USER_CREATE = "user:create";

        /**
         * Authority string for permission to read manager data.
         */
        public static final String MANAGER_CREATE = "manager:read";

        /**
         * Role identifier for users authenticated via OpenID Connect.
         */
        public static final String OIDC_USER = "OIDC_USER";

        /**
         * Role identifier for users authenticated via OAuth2.
         */
        public static final String OAUTH2_USER = "OAUTH2_USER";
    }

    /**
     * Contains constants representing different types of tokens used in authentication.
     */
    public static class TokenType {

        private TokenType() {}

        /**
         * Label for access tokens.
         */
        public static final String ACCESS_TOKEN = "Access token";

        /**
         * Label for refresh tokens.
         */
        public static final String REFRESH_TOKEN = "Refresh token";

        /**
         * Label for reset password tokens.
         */
        public static final String RESET_PASSWORD_TOKEN = "Reset password token";
    }
}
