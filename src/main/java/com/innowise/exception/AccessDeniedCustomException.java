package com.innowise.exception;

import com.innowise.util.ExceptionConstant;

import java.io.Serial;
import java.io.Serializable;

public class AccessDeniedCustomException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    public AccessDeniedCustomException() {
        super(ExceptionConstant.ACCESS_DENIED);
    }

    public AccessDeniedCustomException(String message) {
        super(message);
    }

    public AccessDeniedCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
