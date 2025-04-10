package com.java.assessment.JAVA_ASSESSMENT.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ErrorUtil {

    @Autowired
    @Qualifier("errorMessageSource")
    private MessageSource errorMessageSource;

    public void addError(List<String> errorList, String errorConstant, Object... args) {
        errorList.add(errorMessageSource.getMessage(errorConstant, args, LocaleUtil.getLocale()));
    }

}
