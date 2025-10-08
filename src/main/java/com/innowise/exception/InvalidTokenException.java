package com.innowise.exception;

import com.innowise.util.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

public class InvalidTokenException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super(ExceptionMessage.INVALID_TOKEN.get());
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
