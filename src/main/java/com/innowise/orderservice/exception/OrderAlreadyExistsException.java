package com.innowise.orderservice.exception;

import java.io.Serial;

/**
 * Thrown when trying to create an order that already exists.
 */
public class OrderAlreadyExistsException extends OrderServiceException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OrderAlreadyExistsException() {
        super(ErrorMessage.ORDER_ALREADY_EXISTS);
    }

    public OrderAlreadyExistsException(Throwable cause) {
        super(ErrorMessage.ORDER_ALREADY_EXISTS, cause);
    }
}
