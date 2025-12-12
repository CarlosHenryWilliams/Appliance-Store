package com.appliancestore.sales_service.exception;

public class ProductInsufficientStockException extends RuntimeException {
    public ProductInsufficientStockException(String message) {
        super(message);
    }
}
