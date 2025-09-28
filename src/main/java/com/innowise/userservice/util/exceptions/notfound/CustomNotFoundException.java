package com.innowise.userservice.util.exceptions.notfound;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base class for custom "not found" exceptions.
 */
public abstract class CustomNotFoundException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public CustomNotFoundException(String message) {
        super(message);
    }
}
