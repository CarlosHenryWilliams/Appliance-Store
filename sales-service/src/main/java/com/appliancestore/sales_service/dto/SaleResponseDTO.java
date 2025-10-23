package com.appliancestore.sales_service.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDTO {
    //Sale
    private Long idSale;
    private LocalDate saleDate;
    private Double totalPrice;
    //CART
    private Long idCart;
    private List<ProductDTO> productDetailsResponseDTOList;

}
