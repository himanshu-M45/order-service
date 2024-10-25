package org.example.orderservice.Exceptions;

public class InvalidRestaurantIdException extends RuntimeException {
    public InvalidRestaurantIdException(String message) {
        super(message);
    }
}
