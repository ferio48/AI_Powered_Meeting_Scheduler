package com.aipowered.meeting.scheduler.constant;

/**
 * Centralized container for application-specific error codes.
 *
 * These constants represent message keys that can be used to fetch
 * localized error messages from message resource bundles (e.g., messages.properties).
 */
public class ErrorConstants {

    private ErrorConstants() {}

    /**
     * Error key for when a user already exists with the provided email address.
     */
    public static final String USER_PRESENT_EMAIL_ADDRESS_ERROR = "USER_PRESENT_EMAIL_ADDRESS_ERROR";

    /**
     * Error key for when a field does not match the required format or pattern.
     */
    public static final String FIELD_FORMAT_ERROR = "FIELD_FORMAT_ERROR";

    /**
     * Error key for when a field is blank or null.
     */
    public static final String FIELD_BLANK = "FIELD_BLANK";

    /**
     * Error key for when two fields that are expected to be equal do not match (e.g., password and confirm password).
     */
    public static final String FIELDS_NOT_EQUAL_ERROR = "FIELDS_NOT_EQUAL_ERROR";
}
