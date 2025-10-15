package com.appliancestore.carts_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsResponseDTO {
    private Long idProduct;
    private String name;
    private String brand;
    private Double price;
    private int quantity;
}
