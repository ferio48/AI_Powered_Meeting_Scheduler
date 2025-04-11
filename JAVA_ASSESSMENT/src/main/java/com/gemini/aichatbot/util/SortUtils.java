package com.gemini.aichatbot.util;

import org.springframework.data.domain.Sort;

/**
 * Utility class for building {@link Sort} objects based on user input.
 *
 * This utility is commonly used to handle dynamic sorting in pageable
 * API requests or repository queries.
 */
public class SortUtils {

    /**
     * Constructs a {@link Sort} object based on the provided sorting parameters.
     *
     * @param sortBy   the field name to sort by (e.g., "createdDate", "name")
     * @param sortDir  the direction of sorting; should be either "asc" or "desc"
     * @return a {@link Sort} object representing the desired sort order
     */
    public static Sort getSort(final String sortBy, final String sortDir) {
        return sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}
