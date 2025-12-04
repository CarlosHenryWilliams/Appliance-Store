package com.appliancestore.sales_service.service.integration;

import com.appliancestore.sales_service.dto.CartDTO;
import com.appliancestore.sales_service.dto.InventoryUpdateDTO;
import com.appliancestore.sales_service.dto.ProductDTO;
import com.appliancestore.sales_service.exception.ProductServiceUnavailableException;
import com.appliancestore.sales_service.repository.IProductAPI;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductIntegrationService {
    @Autowired
    private IProductAPI productAPI;

    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackSubtractProductQuantity")
    public void subtractProductQuantity(CartDTO cartDTO){
        for (ProductDTO product : cartDTO.getProductDetailsResponseDTOList()){
            productAPI.subtractProductQuantity(
                    new InventoryUpdateDTO(product.getIdProduct(), product.getQuantity())
            );
        }
    }

    private void fallbackSubtractProductQuantity(CartDTO cartDTO, Throwable throwable){
        throw new ProductServiceUnavailableException("Fallo en el servicio de productos (resta de stock).");
    }

    @CircuitBreaker(name = "products-service", fallbackMethod = "fallbackAddProductQuantity")
    public void addProductQuantity(CartDTO cartDTO){
        for (ProductDTO productDTO : cartDTO.getProductDetailsResponseDTOList()) {
            productAPI.addProductQuantity(
                    new InventoryUpdateDTO(productDTO.getIdProduct(), productDTO.getQuantity())
            );
        }
    }

    private void fallbackAddProductQuantity(CartDTO cartDTO, Throwable throwable){
        throw new ProductServiceUnavailableException("Fallo en el servicio de productos (suma de stock).");
    }
}
