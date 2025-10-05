package com.innowise.userservice.exceptions.conflict;

import java.io.Serial;
import java.io.Serializable;

public class CustomConflictException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomConflictException(String message) {
        super(message);
    }
}
