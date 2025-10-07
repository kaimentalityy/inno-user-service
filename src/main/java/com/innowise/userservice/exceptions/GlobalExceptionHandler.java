package com.innowise.userservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.innowise.userservice.model.dto.ErrorDto;
import com.innowise.userservice.exceptions.badrequest.CustomBadRequestException;
import com.innowise.userservice.exceptions.conflict.CustomConflictException;
import com.innowise.userservice.exceptions.notfound.CustomNotFoundException;

/**
 * Global exception handler.
 * <p>
 * Handles custom business exceptions and validation errors,
 * returning {@link ErrorDto} with appropriate HTTP status.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** Handles bad request exceptions (HTTP 400). */
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(CustomBadRequestException e) {
        log.warn("Bad request: {}", e.getMessage());
        return buildErrorDto("Bad request: " + e.getMessage());
    }

    /** Handles conflict exceptions (HTTP 409). */
    @ExceptionHandler(CustomConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleConflictException(CustomConflictException e) {
        log.warn("Conflict: {}", e.getMessage());
        return buildErrorDto("Conflict: " + e.getMessage());
    }

    /** Handles not found exceptions (HTTP 404). */
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(CustomNotFoundException e) {
        log.warn("Not found: {}", e.getMessage());
        return buildErrorDto("Not found: " + e.getMessage());
    }

    /** Handles validation errors (HTTP 400). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation failed");

        log.warn("Validation error: {}", message);
        return new ErrorDto(message);
    }

    /** Helper to create {@link ErrorDto} with a message. */
    private ErrorDto buildErrorDto(String message) {
        return new ErrorDto(message);
    }
}
