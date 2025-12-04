package com.appliancestore.carts_service.service.integration;

import com.appliancestore.carts_service.dto.ProductDTO;
import com.appliancestore.carts_service.exception.ProductServiceUnavailableException;
import com.appliancestore.carts_service.repository.IProductAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductIntegrationService {
    @Autowired
    private IProductAPI productAPI;

    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackFindAllProductsByIds")
    @Retry(name = "products-service")
    public List<ProductDTO> findAllProductsByIds(List<Long> arrayIds){
      return productAPI.findAllProductsByIds(arrayIds);
    }

    private List<ProductDTO> fallbackFindAllProductsByIds(List<Long> arrayIds, Throwable throwable){
        throw new ProductServiceUnavailableException("Could not found products, Product Service unavailable.");
    }


}
