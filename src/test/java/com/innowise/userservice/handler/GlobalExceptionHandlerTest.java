package com.innowise.userservice.handler;

import com.innowise.userservice.exceptions.badrequest.CustomBadRequestException;
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
        assertTrue(dto.message().contains("invalid input"));
    }

    @Test
    void testHandleValidationErrors() {
        Object target = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "objectName");
        bindingResult.addError(new FieldError("objectName", "field1", "must not be null"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ErrorDto dto = handler.handleValidationErrors(ex);
        assertTrue(dto.message().contains("field1"));
    }
}
