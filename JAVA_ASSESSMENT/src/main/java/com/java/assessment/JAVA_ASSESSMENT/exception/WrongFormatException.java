package com.java.assessment.JAVA_ASSESSMENT.exception;

public class WrongFormatException extends RuntimeException{

    /**
     * Constructs a new WrongFormatException with no detail message.
     */
    public WrongFormatException() {
        super();
    }

    /**
     * Constructs a new WrongFormatException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     */
    public WrongFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new WrongFormatException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public WrongFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new WrongFormatException with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     */
    public WrongFormatException(Throwable cause) {
        super(cause);
    }


}
