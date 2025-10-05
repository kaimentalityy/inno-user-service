package com.innowise.userservice.handler;

import com.innowise.userservice.exceptions.badrequest.CustomBadRequestException;
import com.innowise.userservice.exceptions.conflict.CustomConflictException;
import com.innowise.userservice.exceptions.notfound.CustomNotFoundException;
import com.innowise.userservice.model.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleBadRequestException() {
        CustomBadRequestException ex = new CustomBadRequestException("invalid input");
        ErrorDto dto = handler.handleBadRequestException(ex);
        assertNotNull(dto);
        assertTrue(dto.message().contains("invalid input"));
    }

    @Test
    void testHandleConflictException() {
        CustomConflictException ex = new CustomConflictException("duplicate entity");
        ErrorDto dto = handler.handleConflictException(ex);
        assertNotNull(dto);
        assertTrue(dto.message().contains("duplicate entity"));
    }

    @Test
    void testHandleNotFoundException() {
        CustomNotFoundException ex = new CustomNotFoundException("entity not found");
        ErrorDto dto = handler.handleNotFoundException(ex);
        assertNotNull(dto);
        assertTrue(dto.message().contains("entity not found"));
    }

    @Test
    void testHandleValidationErrors() {
        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "must not be null"));
        bindingResult.addError(new FieldError("objectName", "field2", "must be positive"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ErrorDto dto = handler.handleValidationErrors(ex);

        assertNotNull(dto);
        assertTrue(dto.message().contains("field1"));
        assertTrue(dto.message().contains("field2"));
    }
}
