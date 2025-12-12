package com.appliancestore.sales_service.service.integration;

import com.appliancestore.sales_service.dto.CartDTO;
import com.appliancestore.sales_service.exception.CartServiceUnavailableException;
import com.appliancestore.sales_service.exception.ProductServiceUnavailableException;
import com.appliancestore.sales_service.repository.ICartAPI;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartIntegrationService {
    @Autowired
    private ICartAPI cartAPI;

    @Retry(name = "carts-service")
    @CircuitBreaker(name="carts-service", fallbackMethod="fallbackFindCartById")
    public CartDTO findCartById(Long idCart){
        return cartAPI.findCartById(idCart);
    }

    private CartDTO fallbackFindCartById(Long idCart, Throwable t){

        // Get errorMessage
        String errorMessage = t.getMessage().toLowerCase();
        System.out.println(errorMessage);

        // If it is a FeignException.NotFound (404)
        if (t instanceof FeignException.NotFound) {
            throw (FeignException.NotFound) t;
        }

        //  // If it is a FeignException.Conflict (409)
        if (t instanceof FeignException.Conflict) {
            throw (FeignException.Conflict) t;  // (Cast "t" to his exception).
        }


        // If the product service is unavailable
        if ((errorMessage.contains("product") || errorMessage.contains("products")) && errorMessage.contains("unavailable")) {
            throw new ProductServiceUnavailableException(
                    "Product Service unavailable. Cart Service could not retrieve product details."
            );
        }
        throw new CartServiceUnavailableException("Cart Service Unavailable, Cart not found");
    }
}
