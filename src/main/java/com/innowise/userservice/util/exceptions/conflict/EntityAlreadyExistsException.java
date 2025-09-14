package com.innowise.userservice.util.exceptions.conflict;

public class EntityAlreadyExistsException extends CustomConflictException {
    public EntityAlreadyExistsException(String message) {
        super("Entity " + message + " already exists");
    }
}
