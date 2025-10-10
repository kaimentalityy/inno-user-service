package com.innowise.userservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing standardized error responses returned by the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Corresponding HTTP status code.
     */
    private int status;
}
