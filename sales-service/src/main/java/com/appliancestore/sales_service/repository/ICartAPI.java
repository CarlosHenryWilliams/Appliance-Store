package com.appliancestore.sales_service.repository;

import com.appliancestore.sales_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carts-service")
public interface ICartAPI {
    @GetMapping("api/v1/carts/{idCart}")
    public CartDTO findCartById(@PathVariable Long idCart);
}
