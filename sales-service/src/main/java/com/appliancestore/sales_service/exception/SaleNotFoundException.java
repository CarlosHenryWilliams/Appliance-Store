package com.appliancestore.sales_service.exception;


public class SaleNotFoundException  extends  RuntimeException{
    public SaleNotFoundException(String message) {
        super(message);
    }
}
