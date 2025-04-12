package com.aipowered.meeting.scheduler.exception;

public class OperationFailedException extends RuntimeException{

    /**
     * Constructs a new OperationFailedException with no detail message.
     */
    public OperationFailedException() {
        super();
    }

    /**
     * Constructs a new OperationFailedException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new OperationFailedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public OperationFailedException(String message) {
        super(message);
    }

    /**
     * Constructs a new OperationFailedException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public OperationFailedException(Throwable cause) {
        super(cause);
    }


}
