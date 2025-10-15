package com.appliancestore.carts_service.repository;

import com.appliancestore.carts_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "products-service")
public interface IProductAPI {
    @GetMapping("/api/v1/products")
    List<ProductDTO> findAllProductsAPI();
}
