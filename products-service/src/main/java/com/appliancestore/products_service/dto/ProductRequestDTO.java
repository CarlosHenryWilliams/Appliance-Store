package com.appliancestore.products_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String brand;
    private Double price;
    private int stock;
}
