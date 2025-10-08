package com.innowise.exception;

import com.innowise.util.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

public class UserNotFoundException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;

    public UserNotFoundException() {
        super(ExceptionMessage.USER_NOT_FOUND.get());
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
