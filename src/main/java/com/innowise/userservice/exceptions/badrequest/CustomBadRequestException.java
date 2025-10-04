package com.innowise.userservice.exceptions.badrequest;

import java.io.Serial;
import java.io.Serializable;

public class CustomBadRequestException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomBadRequestException(String message) {
        super(message);
    }
}
