package org.example.orderservice.Exceptions;

public class FailedToAddOrderItemException extends RuntimeException {
    public FailedToAddOrderItemException(String message) {
        super(message);
    }
}
