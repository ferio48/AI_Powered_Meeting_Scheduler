package com.gemini.aichatbot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility component for handling and adding localized error messages.
 *
 * This class helps populate error messages based on message codes and parameters,
 * using a specific {@link MessageSource} for error-related messages.
 */
@Component
public class ErrorUtil {

    /** Message source used to resolve error codes into localized error messages */
    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource errorMessageSource;

    /**
     * Adds a localized error message to the given error list.
     *
     * @param errorList    the list to which the error message will be added
     * @param errorConstant the message key/code defined in the message properties file
     * @param args         optional arguments to fill placeholders in the error message
     */
    public void addError(List<String> errorList, String errorConstant, Object... args) {
        errorList.add(errorMessageSource.getMessage(errorConstant, args, LocaleUtil.getLocale()));
    }
}
