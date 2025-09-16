package com.innowise.userservice.util.exceptions.badrequest;

public class CustomConstraintViolationException extends CustomBadRequestException {
    public CustomConstraintViolationException(String message) {
        super(message);
    }
}