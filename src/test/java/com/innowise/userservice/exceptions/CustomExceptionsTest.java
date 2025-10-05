package com.innowise.userservice.exceptions;

import com.innowise.userservice.exceptions.badrequest.CustomBadRequestException;
import com.innowise.userservice.exceptions.badrequest.CustomConstraintViolationException;
import com.innowise.userservice.exceptions.conflict.CustomConflictException;
import com.innowise.userservice.exceptions.conflict.EntityAlreadyExistsException;
import com.innowise.userservice.exceptions.notfound.CustomNotFoundException;
import com.innowise.userservice.exceptions.notfound.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomExceptionsTest {

    @Test
    void testCustomBadRequestException() {
        CustomBadRequestException ex = new CustomBadRequestException("bad request");
        assertEquals("bad request", ex.getMessage());
    }

    @Test
    void testCustomConstraintViolationException() {
        CustomConstraintViolationException ex = new CustomConstraintViolationException("constraint violated");
        assertEquals("constraint violated", ex.getMessage());
    }

    @Test
    void testCustomConflictException() {
        CustomConflictException ex = new CustomConflictException("conflict happened");
        assertEquals("conflict happened", ex.getMessage());
    }

    @Test
    void testEntityAlreadyExistsException() {
        EntityAlreadyExistsException ex = new EntityAlreadyExistsException("User");
        assertEquals("Entity User already exists", ex.getMessage());
    }

    @Test
    void testCustomNotFoundException() {
        CustomNotFoundException ex = new CustomNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
    }

    @Test
    void testEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("User", "id", 1);
        assertEquals("User with id 1 not found", ex.getMessage());
    }
}
