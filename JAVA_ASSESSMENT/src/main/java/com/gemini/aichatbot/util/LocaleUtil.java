package com.gemini.aichatbot.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Utility class for retrieving the current user's locale context.
 *
 * This is typically used to fetch localized messages using Spring's {@link LocaleContextHolder}.
 */
public class LocaleUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private LocaleUtil() {}

    /**
     * Retrieves the current locale from the Spring context.
     *
     * @return the current {@link Locale} associated with the request or thread
     */
    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
