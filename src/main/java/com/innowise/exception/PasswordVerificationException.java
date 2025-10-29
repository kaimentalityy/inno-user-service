package com.innowise.exception;



import com.innowise.util.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thrown when password verification (matching) fails.
 */
public class PasswordVerificationException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    public PasswordVerificationException() {
        super(ExceptionMessage.PASSWORD_VERIFICATION.get());
    }

    public PasswordVerificationException(String message) {
        super(message);
    }

    public PasswordVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
