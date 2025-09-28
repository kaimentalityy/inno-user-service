package com.innowise.userservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.innowise.userservice.presentation.dto.ErrorDto;
import com.innowise.userservice.util.exceptions.badrequest.CustomBadRequestException;
import com.innowise.userservice.util.exceptions.conflict.CustomConflictException;
import com.innowise.userservice.util.exceptions.notfound.CustomNotFoundException;

/**
 * Global exception handler for the application.
 * <p>
 * Handles custom exceptions and validation errors globally
 * and returns structured {@link ErrorDto} responses
 * with appropriate HTTP status codes.
 * </p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles {@link CustomBadRequestException}.
     *
     * @param e the custom bad request exception
     * @return an {@link ErrorDto} with error message and HTTP 400 status
     */
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(CustomBadRequestException e) {
        log.error("Bad request error occurred: {}", e.getMessage(), e);
        return buildErrorDto("Bad request: " + e.getMessage());
    }

    /**
     * Handles {@link CustomConflictException}.
     *
     * @param e the custom conflict exception
     * @return an {@link ErrorDto} with error message and HTTP 409 status
     */
    @ExceptionHandler(CustomConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleConflictException(CustomConflictException e) {
        log.error("Conflict error occurred: {}", e.getMessage(), e);
        return buildErrorDto("Conflict: " + e.getMessage());
    }

    /**
     * Handles {@link CustomNotFoundException}.
     *
     * @param e the custom not found exception
     * @return an {@link ErrorDto} with error message and HTTP 404 status
     */
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(CustomNotFoundException e) {
        log.error("Not found error occurred: {}", e.getMessage(), e);
        return buildErrorDto("Not found: " + e.getMessage());
    }

    /**
     * Handles validation errors from request DTOs annotated with {@link jakarta.validation.Valid}.
     * <p>
     * Extracts field error messages from {@link MethodArgumentNotValidException}
     * and concatenates them into a single error response.
     * </p>
     *
     * @param ex the exception thrown when validation fails
     * @return an {@link ErrorDto} containing all validation error messages and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation failed");

        return new ErrorDto(message);
    }

    public ErrorDto buildErrorDto(String message) {
        return new ErrorDto(message);
    }
}
