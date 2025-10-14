package com.appliancestore.carts_service.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long idProduct;
    private String name;
    private String brand;
    private Double price;
    private int stock;
}
