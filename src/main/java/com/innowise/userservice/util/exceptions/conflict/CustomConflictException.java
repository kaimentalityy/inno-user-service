package com.innowise.userservice.util.exceptions.conflict;

public abstract class CustomConflictException extends RuntimeException {
    public CustomConflictException(String message) {
        super(message);
    }
}