package com.innowise.userservice.util.exceptions.badrequest;

import java.io.Serial;
import java.io.Serializable;

public abstract class CustomBadRequestException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomBadRequestException(String message) {
        super(message);
    }
}
