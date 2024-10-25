package org.example.orderservice.Exceptions;

public class CannotUpdateOrderStatusException extends RuntimeException {
    public CannotUpdateOrderStatusException(String message) {
        super(message);
    }
}
