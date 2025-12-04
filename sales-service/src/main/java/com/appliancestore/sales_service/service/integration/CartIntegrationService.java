package com.appliancestore.sales_service.service.integration;

import com.appliancestore.sales_service.dto.CartDTO;
import com.appliancestore.sales_service.exception.CartServiceUnavailableException;
import com.appliancestore.sales_service.exception.ProductServiceUnavailableException;
import com.appliancestore.sales_service.repository.ICartAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartIntegrationService {
    @Autowired
    private ICartAPI cartAPI;

    @CircuitBreaker(name="carts-service", fallbackMethod="fallbackFindCartById")
    public CartDTO findCartById(Long idCart){
        return cartAPI.findCartById(idCart);
    }

    private CartDTO fallbackFindCartById(Long idCart, Throwable t){

        // Get errorMessage
        String errorMessage = t.getMessage() != null ? t.getMessage().toLowerCase() : "";
        System.out.println(errorMessage);
        // If the product service is unavailable
        if ((errorMessage.contains("product") || errorMessage.contains("products")) && errorMessage.contains("unavailable")) {
            throw new ProductServiceUnavailableException(
                    "Product Service unavailable. Cart Service could not retrieve product details."
            );
        }
        throw new CartServiceUnavailableException("Cart Service Unavailable, Cart not found");
    }
}
