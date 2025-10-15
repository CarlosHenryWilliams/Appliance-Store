package com.appliancestore.products_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryUpdateDTO {
    private Long idProduct;
    private int quantity;
}
