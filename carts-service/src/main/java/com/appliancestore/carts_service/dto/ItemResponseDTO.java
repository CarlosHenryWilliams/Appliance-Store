package com.appliancestore.carts_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ItemResponseDTO {
    private Long idItem;
    private Long idProduct;
    private int quantity;
}
