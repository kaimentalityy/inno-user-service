package com.innowise.userservice.exceptions.badrequest;

import java.io.Serial;
import java.io.Serializable;

public class CustomConstraintViolationException extends CustomBadRequestException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomConstraintViolationException(String message) {
        super(message);
    }
}
