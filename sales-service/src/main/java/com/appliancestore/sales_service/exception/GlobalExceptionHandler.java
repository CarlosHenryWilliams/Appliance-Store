package com.appliancestore.sales_service.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handler for the 404 (Not Found) error coming from a Feign Client
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<String> handleFeignNotFound(FeignException.NotFound ex) {
        // Extract the relevant part of the message (For example: The cart with the ID: 6 wasn't found.) using contentUTF8
        String errorMessage = "Resource Not Found during microservice call: " + ex.contentUTF8();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // CODE 404
    }

    // Handler for the 409 (Conflict) error coming from a Feign Client
    @ExceptionHandler(FeignException.Conflict.class)
    public ResponseEntity<String> handleFeignConflict(FeignException.Conflict ex) {
        // Extract the relevant part of the message  using contentUTF8
        String errorMessage = "Oops. FeignExceptionConflict:  " + ex.contentUTF8();
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SaleNotFoundException.class)
    public ResponseEntity<String> handleSaleNotFoundException(SaleNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND); // CODE 404
    }
    // CartService Unavailable
    @ExceptionHandler(CartServiceUnavailableException.class)
    public ResponseEntity<String> handleCartServiceUnavailableException(CartServiceUnavailableException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE); // CODE 503
    }
    // CartService Unavailable
    @ExceptionHandler(ProductServiceUnavailableException.class)
    public ResponseEntity<String> handleProductServiceUnavailableException(ProductServiceUnavailableException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE); // CODE 503
    }

    @ExceptionHandler(ProductInsufficientStockException.class)
    public ResponseEntity<String> handleProductInsufficientStockException(ProductInsufficientStockException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT); // CODE 409
    }

}
