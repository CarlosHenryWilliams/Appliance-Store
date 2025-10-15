package com.appliancestore.carts_service.repository;

import com.appliancestore.carts_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "products-service")
public interface IProductAPI {
    @GetMapping("api/v1/products")
    List<ProductDTO> findAllProductsAPI();

    @GetMapping("api/v1/products/{idProduct}")
    public ProductDTO findProductById(@PathVariable Long idProduct);

}
