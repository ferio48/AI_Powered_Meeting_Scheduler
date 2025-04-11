package com.gemini.aichatbot.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.gemini.aichatbot.exception.ResourceNotFoundException;
import com.gemini.aichatbot.model.response.BasicRestResponse;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.ServletException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.rmi.ServerException;
import java.sql.Timestamp;

/**
 * Global exception handler to handle all exceptions in the application.
 * <p>
 * This class uses Spring's {@link ControllerAdvice} to handle exceptions thrown by any controller.
 * It provides methods to handle specific types of exceptions and return custom error responses.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler implements ApplicationEventPublisherAware {

    protected ApplicationEventPublisher eventPublisher;

    /**
     * Sets the {@link ApplicationEventPublisher} that this object runs in.
     *
     * @param applicationEventPublisher the event publisher to be used by this object.
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    /**
     * Handle {@link MethodArgumentNotValidException}.
     * <p>
     * This method handles exceptions thrown when method argument validation fails.
     * It constructs a {@link BasicRestResponse} containing the error details.
     * </p>
     *
     * @param ex the exception thrown when method argument validation fails.
     * @return a {@link BasicRestResponse} containing the error details.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BasicRestResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();
        StringBuilder errorMessage = new StringBuilder();
        BindingResult result = ex.getBindingResult();
        for (FieldError fieldError : result.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage()).append(" ");
        }

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(errorMessage.substring(0, errorMessage.length() - 1));

        return response;
    }

    /**
     * Handle {@link RuntimeException}.
     * <p>
     * This method handles runtime exceptions and constructs a {@link BasicRestResponse} containing the error details.
     * </p>
     *
     * @param ex the runtime exception thrown.
     * @return a {@link BasicRestResponse} containing the error details.
     */
    @ExceptionHandler(RuntimeException.class)
    public BasicRestResponse handleRuntimeException(RuntimeException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getLocalizedMessage());

        return response;
    }

    /**
     * Handle {@link ResourceNotFoundException}.
     * <p>
     * This method handles resource not found exceptions and constructs a {@link BasicRestResponse} containing the error details.
     * </p>
     *
     * @param ex the resource not found exception thrown.
     * @return a {@link BasicRestResponse} containing the error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public BasicRestResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getLocalizedMessage());

        return response;
    }

    @ExceptionHandler(NumberFormatException.class)
    public BasicRestResponse handleNumberFormatException(NumberFormatException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Given string value cannot be converted to Integer");

        return response;
    }

    @ExceptionHandler(JsonParseException.class)
    public BasicRestResponse handleJsonProcessingException(JsonParseException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Json Parse error: some field could be in wrong format");

        return response;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BasicRestResponse handleAccessDeniedException(AccessDeniedException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Access Denied");

        return response;
    }

    @ExceptionHandler(ServerException.class)
    public BasicRestResponse handleAccessDeniedException(ServletException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Servlet Exception");

        return response;
    }

    @ExceptionHandler(AuthException.class)
    public BasicRestResponse handleAuthException(AuthException ex) {
        BasicRestResponse response = BasicRestResponse.builder().build();

        response.setTime(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Auth Exception");

        return response;
    }
}