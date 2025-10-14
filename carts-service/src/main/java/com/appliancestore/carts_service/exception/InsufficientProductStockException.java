package com.appliancestore.carts_service.exception;

public class InsufficientProductStockException extends RuntimeException{
    public InsufficientProductStockException(String message) {
        super(message);
    }
}
