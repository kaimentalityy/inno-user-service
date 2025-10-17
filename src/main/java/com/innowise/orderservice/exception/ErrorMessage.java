package com.innowise.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Centralized error message constants for the Order Service.
 * These messages are used by custom exceptions and global exception handlers.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {

    INTERNAL_ERROR("An unexpected internal error occurred"),
    INVALID_REQUEST("Invalid request parameters"),
    ORDER_NOT_FOUND("Order not found"),
    ORDER_ALREADY_EXISTS("Order already exists"),
    ORDER_CONFLICT("Order conflict detected"),
    ORDER_ITEM_NOT_FOUND("Order item not found"),
    PAYMENT_FAILED("Payment processing failed");

    private final String message;
}
