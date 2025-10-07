package com.innowise.exception;

import com.innowise.util.ExceptionConstant;

import java.io.Serial;
import java.io.Serializable;

public class EntityAlreadyExistsException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;

    public EntityAlreadyExistsException() {
        super(ExceptionConstant.ENTITY_ALREADY_EXISTS);
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
