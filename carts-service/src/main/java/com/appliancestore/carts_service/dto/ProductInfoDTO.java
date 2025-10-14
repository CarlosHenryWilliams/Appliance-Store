package com.appliancestore.carts_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProductInfoDTO {
    private Double price;
    private int stock;
}
