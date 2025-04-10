package com.java.assessment.JAVA_ASSESSMENT.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class LocaleUtil {

    private LocaleUtil() {}

    public static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

}
