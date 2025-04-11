package com.gemini.aichatbot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Configuration class for setting up multiple {@link MessageSource} beans
 * used for internationalization (i18n) and message resolution in the application.
 */
@Configuration
public class MessageSourceConfig {

    /**
     * Bean for resolving error-related messages from the {@code error-messages.properties} file.
     *
     * This source is used for error codes and validation error responses.
     *
     * @return a configured {@link MessageSource} for error messages
     */
    @Bean
    public MessageSource errorMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("error-messages"); // Looks for error-messages.properties
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Bean for resolving general informational or success messages
     * from the {@code messages.properties} file.
     *
     * @return a configured {@link MessageSource} for general messages
     */
    @Bean
    public MessageSource generalMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Looks for messages.properties
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
