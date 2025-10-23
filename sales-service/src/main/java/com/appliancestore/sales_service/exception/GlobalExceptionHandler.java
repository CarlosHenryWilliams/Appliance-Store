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
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
