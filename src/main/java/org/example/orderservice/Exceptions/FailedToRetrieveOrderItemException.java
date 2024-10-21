package org.example.orderservice.Exceptions;

public class FailedToRetrieveOrderItemException extends RuntimeException {
    public FailedToRetrieveOrderItemException(String message) {
        super(message);
    }
}
