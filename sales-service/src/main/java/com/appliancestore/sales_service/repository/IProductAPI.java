package com.appliancestore.sales_service.repository;

import com.appliancestore.sales_service.dto.InventoryUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "products-service")
public interface IProductAPI {
    @PatchMapping("api/v1/products/subtract-stock")
    public ResponseEntity<String> subtractProductQuantity(@RequestBody InventoryUpdateDTO inventoryUpdateDTO);
}
