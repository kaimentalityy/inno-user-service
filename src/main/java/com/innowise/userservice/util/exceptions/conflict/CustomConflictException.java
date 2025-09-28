package com.innowise.userservice.util.exceptions.conflict;

import java.io.Serial;
import java.io.Serializable;

public abstract class CustomConflictException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomConflictException(String message) {
        super(message);
    }
}