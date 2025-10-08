package com.innowise.exception;

import com.innowise.model.dto.resp.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleUserNotFound(UserNotFoundException e) {
        log.warn("User not found: {}", e.getMessage());
        return buildErrorDto("User not found: " + e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleInvalidToken(InvalidTokenException e) {
        log.warn("Invalid token: {}", e.getMessage());
        return buildErrorDto("Invalid token: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleAccessDenied(AccessDeniedCustomException e) {
        log.warn("Access denied: {}", e.getMessage());
        return buildErrorDto("Access denied: " + e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleBadCredentials(BadCredentialsException e) {
        log.warn("Bad credentials: {}", e.getMessage());
        return buildErrorDto("Bad credentials");
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        log.warn("Entity already exists: {}", e.getMessage());
        return buildErrorDto("Entity already exists: " + e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleUsernameNotFound(UsernameNotFoundException e) {
        log.warn("Username not found: {}", e.getMessage());
        return buildErrorDto("Username not found: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation failed");

        log.warn("Validation error: {}", message);
        return buildErrorDto(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleGeneralException(Exception e) {
        log.error("Unexpected error: ", e);
        return buildErrorDto("An unexpected error occurred");
    }

    private ErrorDto buildErrorDto(String message) {
        return new ErrorDto(message);
    }
}
