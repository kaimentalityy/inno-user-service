package com.innowise.userservice.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionsTest {

    @Test
    void testEntityAlreadyExistsException() {
        EntityAlreadyExistsException ex = new EntityAlreadyExistsException("User", "email", "test@example.com");
        assertEquals("User with email 'test@example.com' already exists", ex.getMessage());
    }

    @Test
    void testEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("User", "id", 1);
        assertEquals("User with id '1' not found", ex.getMessage());
    }

    @Test
    void testInvalidRequestException() {
        InvalidRequestException ex = new InvalidRequestException("Invalid parameters");
        assertEquals("Invalid parameters", ex.getMessage());
    }

    @Test
    void testCustomConstraintViolationException() {
        CustomConstraintViolationException ex = new CustomConstraintViolationException("constraint violated");
        assertEquals("constraint violated", ex.getMessage());
    }

    @Test
    void testAccessDeniedCustomException() {
        AccessDeniedCustomException ex = new AccessDeniedCustomException();
        assertEquals(ErrorMessage.ACCESS_DENIED.getMessage(), ex.getMessage());
    }
}
