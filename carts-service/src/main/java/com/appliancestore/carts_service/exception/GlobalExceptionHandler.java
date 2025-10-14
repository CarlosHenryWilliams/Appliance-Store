package com.appliancestore.carts_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientProductStockException.class)
    public ResponseEntity<String> handleInsufficientProductStock(InsufficientProductStockException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleInsufficientProductStock(ProductNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
