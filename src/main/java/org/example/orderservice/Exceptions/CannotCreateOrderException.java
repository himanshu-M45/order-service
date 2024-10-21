package org.example.orderservice.Exceptions;

public class CannotCreateOrderException extends RuntimeException {
    public CannotCreateOrderException(String message) {
        super(message);
    }
}
