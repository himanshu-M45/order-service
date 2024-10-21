package org.example.orderservice.Exceptions;

import org.example.orderservice.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CannotCreateOrderException.class)
    public ResponseEntity<ResponseDTO<String>> handleCannotCreateOrderException(CannotCreateOrderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(CannotAddOrderItemException.class)
    public ResponseEntity<ResponseDTO<String>> handleCannotAddOrderItemException(CannotAddOrderItemException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(FailedToAddOrderItemException.class)
    public ResponseEntity<ResponseDTO<String>> handleOrderItemsNotAvailableException(FailedToAddOrderItemException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ResponseDTO<>(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage()));
    }
}
