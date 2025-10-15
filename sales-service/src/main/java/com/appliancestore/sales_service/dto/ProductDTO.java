package com.appliancestore.sales_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long idProduct;
    private String name;
    private String brand;
    private Double price;
    private int quantity;
}