package org.example.orderservice.Exceptions;

public class NoOrderItemsSelectedException extends RuntimeException {
    public NoOrderItemsSelectedException(String message) {
        super(message);
    }
}
