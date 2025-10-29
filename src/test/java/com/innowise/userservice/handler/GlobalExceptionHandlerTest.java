package com.innowise.userservice.handler;

import com.innowise.userservice.exception.*;
import com.innowise.userservice.model.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleBadRequestException() {
        CustomBadRequestException ex = new CustomBadRequestException("invalid input");
        ErrorDto dto = handler.handleApplicationException(ex);

        assertNotNull(dto);
        assertTrue(dto.getMessage().contains("Invalid request") || dto.getMessage().contains("invalid input"));
        assertEquals(400, dto.getStatus());
    }

    @Test
    void testHandleConflictException() {
        CustomConflictException ex = new CustomConflictException("duplicate entity");
        ErrorDto dto = handler.handleApplicationException(ex);

        assertNotNull(dto);
        assertTrue(dto.getMessage().contains("Entity already exists") || dto.getMessage().contains("duplicate entity"));
        assertEquals(409, dto.getStatus());
    }

    @Test
    void testHandleNotFoundException() {
        CustomNotFoundException ex = new CustomNotFoundException("entity not found");
        ErrorDto dto = handler.handleApplicationException(ex);

        assertNotNull(dto);
        assertTrue(dto.getMessage().contains("Entity with specified identifier not found")
                || dto.getMessage().contains("entity not found"));
        assertEquals(404, dto.getStatus());
    }

    @Test
    void testHandleValidationErrors() throws NoSuchMethodException {
        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "must not be null"));
        bindingResult.addError(new FieldError("objectName", "field2", "must be positive"));

        Method method = getClass().getDeclaredMethod("testHandleValidationErrors");
        MethodParameter methodParameter = new MethodParameter(method, -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ErrorDto dto = handler.handleValidationErrors(ex);

        assertNotNull(dto);
        assertTrue(dto.getMessage().contains("field1"));
        assertTrue(dto.getMessage().contains("field2"));
        assertEquals(400, dto.getStatus());
    }

    @Test
    void testHandleUnexpectedException() {
        RuntimeException ex = new RuntimeException("unexpected error");
        ErrorDto dto = handler.handleUnexpected(ex);

        assertNotNull(dto);
        assertEquals(500, dto.getStatus());
        assertTrue(dto.getMessage().toLowerCase().contains("internal")
                || dto.getMessage().toLowerCase().contains("unexpected"));
    }

    // ---- Inner test exception classes ----

    static class CustomBadRequestException extends ApplicationException {
        public CustomBadRequestException(String message) {
            super(ErrorMessage.INVALID_REQUEST, message);
        }
    }

    static class CustomConflictException extends ApplicationException {
        public CustomConflictException(String message) {
            super(ErrorMessage.ENTITY_ALREADY_EXISTS, message);
        }
    }

    static class CustomNotFoundException extends ApplicationException {
        public CustomNotFoundException(String message) {
            super(ErrorMessage.ENTITY_NOT_FOUND, message);
        }
    }
}
