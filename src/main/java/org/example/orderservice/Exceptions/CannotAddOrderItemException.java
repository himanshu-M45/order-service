package org.example.orderservice.Exceptions;

public class CannotAddOrderItemException extends RuntimeException {
    public CannotAddOrderItemException(String message) {
        super(message);
    }
}
