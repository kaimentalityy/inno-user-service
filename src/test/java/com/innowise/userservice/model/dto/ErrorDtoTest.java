package com.innowise.userservice.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorDtoTest {

    @Test
    void lombokGeneratedMethodsWork() {
        ErrorDto error = new ErrorDto();
        error.setMessage("Something went wrong");
        error.setStatus(400);

        assertEquals("Something went wrong", error.getMessage());
        assertEquals(400, error.getStatus());

        ErrorDto error2 = new ErrorDto("Error!", 500);
        assertEquals("Error!", error2.getMessage());
        assertEquals(500, error2.getStatus());
    }
}
