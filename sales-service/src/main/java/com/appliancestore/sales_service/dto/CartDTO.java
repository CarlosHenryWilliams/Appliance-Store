package com.appliancestore.sales_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long idCart;
    private List<ProductDTO> productDetailsResponseDTOList;
    private Double totalPrice;
}
