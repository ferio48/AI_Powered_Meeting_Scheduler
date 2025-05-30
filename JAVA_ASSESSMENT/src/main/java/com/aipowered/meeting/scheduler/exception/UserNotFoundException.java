package com.aipowered.meeting.scheduler.exception;

public class UserNotFoundException extends RuntimeException{

    /**
     * Constructs a new UserNotFoundException with no detail message.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserNotFoundException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

}
