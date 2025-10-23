package com.appliancestore.carts_service.repository;

import com.appliancestore.carts_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products-service")
public interface IProductAPI {
    @GetMapping("api/v1/products")
    List<ProductDTO> findAllProductsAPI();

    @PostMapping("api/v1/products/by-ids")
    List<ProductDTO> findAllProductsByIds(@RequestBody List<Long> idProductsList);

    @GetMapping("api/v1/products/{idProduct}")
    public ProductDTO findProductById(@PathVariable Long idProduct);


}
