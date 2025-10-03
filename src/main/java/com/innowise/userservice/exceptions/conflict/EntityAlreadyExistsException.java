package com.innowise.userservice.exceptions.conflict;

import java.io.Serial;
import java.io.Serializable;

public class EntityAlreadyExistsException extends CustomConflictException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(String message) {
        super("Entity " + message + " already exists");
    }
}
