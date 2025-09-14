package com.innowise.userservice.util.exceptions.notfound;

public abstract class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(String message) {
        super(message);
    }
}
