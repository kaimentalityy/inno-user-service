package com.innowise.exception;

import com.innowise.util.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

public class AccessDeniedCustomException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    public AccessDeniedCustomException() {
        super(ExceptionMessage.ACCESS_DENIED.get());
    }

    public AccessDeniedCustomException(String message) {
        super(message);
    }

    public AccessDeniedCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
