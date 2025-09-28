package com.innowise.userservice.util.exceptions.notfound;

import java.io.Serial;
import java.io.Serializable;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends CustomNotFoundException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entity, Object obj, Object name) {
        super(entity + " with " + obj + " " + name + " not found");
    }
}
