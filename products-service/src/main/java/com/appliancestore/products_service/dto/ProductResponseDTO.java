package com.appliancestore.products_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProductResponseDTO {
    private Long idProduct;
    private String name;
    private String brand;
    private Double price;
    private int stock;
}
