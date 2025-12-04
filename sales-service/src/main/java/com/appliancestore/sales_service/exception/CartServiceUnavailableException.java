package com.appliancestore.sales_service.exception;

public class CartServiceUnavailableException extends RuntimeException {
    public CartServiceUnavailableException(String message) {
        super(message);
    }
}
