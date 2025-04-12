package com.aipowered.meeting.scheduler.util;

import com.aipowered.meeting.scheduler.security.model.AuthenticationDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class to extract information about the currently logged-in user
 * from the Spring Security context.
 *
 * This includes:
 * - Logged-in user ID
 * - Logged-in username
 * - Logged-in user role
 *
 * Relies on the assumption that {@link AuthenticationDetails} is set as authentication details
 * within the Spring Security context.
 */
@Component
public class LoggedInUserUtil {

    /**
     * Retrieves the role of the currently logged-in user.
     *
     * @return the role of the user as a {@link String}
     * @throws RuntimeException if authentication details are missing or invalid
     */
    public static String getLoggedInUserRole() {
        try {
            final AuthenticationDetails authDetails = (AuthenticationDetails)
                    SecurityContextHolder.getContext().getAuthentication().getDetails();
            return authDetails.getLoggedInUserRole();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while extracting role of logged-in user");
        }
    }

    /**
     * Retrieves the ID of the currently logged-in user.
     *
     * @return the user ID as an {@link Integer}
     * @throws RuntimeException if authentication details are missing or invalid
     */
    public static Integer getLoggedInUserID() {
        try {
            final AuthenticationDetails authDetails = (AuthenticationDetails)
                    SecurityContextHolder.getContext().getAuthentication().getDetails();
            return authDetails.getLoggedInUserId();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while extracting user ID of logged-in user");
        }
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return the username as a {@link String}
     * @throws RuntimeException if authentication details are missing or invalid
     */
    public static String getLoggedInUsername() {
        try {
            final AuthenticationDetails authDetails = (AuthenticationDetails)
                    SecurityContextHolder.getContext().getAuthentication().getDetails();
            return authDetails.getLoggedInUsername();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while extracting username of logged-in user");
        }
    }
}
