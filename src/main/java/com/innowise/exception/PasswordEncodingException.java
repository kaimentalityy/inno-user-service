package com.innowise.exception;

import com.innowise.util.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thrown when password encoding fails.
 */
public class PasswordEncodingException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordEncodingException() {
        super(ExceptionMessage.PASSWORD_ENCODING.get());
    }

    public PasswordEncodingException(String message) {
        super(message);
    }

    public PasswordEncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
