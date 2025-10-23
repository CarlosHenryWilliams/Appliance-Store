package com.appliancestore.sales_service.repository;

import com.appliancestore.sales_service.dto.InventoryUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "products-service")
public interface IProductAPI {
    @PutMapping("api/v1/products/subtract-stock")
    public void subtractProductQuantity(@RequestBody InventoryUpdateDTO inventoryUpdateDTO);
    @PutMapping("api/v1/products/add-stock")
    public void addProductQuantity(@RequestBody InventoryUpdateDTO inventoryUpdateDTO);
}
