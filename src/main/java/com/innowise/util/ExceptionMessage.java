package com.innowise.util;

public enum ExceptionMessage {

    ENTITY_ALREADY_EXISTS("Entity already exists"),
    INVALID_TOKEN("Token invalid"),
    PASSWORD_ENCODING("Error encoding password"),
    PASSWORD_VERIFICATION("Error verifying password"),
    USER_NOT_FOUND("User not found"),
    ACCESS_DENIED("Access denied");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}
