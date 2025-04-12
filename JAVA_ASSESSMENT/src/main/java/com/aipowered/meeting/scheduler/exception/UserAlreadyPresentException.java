package com.aipowered.meeting.scheduler.exception;


public class UserAlreadyPresentException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyPresentException with no detail message.
     */
    public UserAlreadyPresentException() {
        super();
    }

    /**
     * Constructs a new UserAlreadyPresentException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public UserAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new UserAlreadyPresentException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public UserAlreadyPresentException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserAlreadyPresentException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public UserAlreadyPresentException(Throwable cause) {
        super(cause);
    }


}
