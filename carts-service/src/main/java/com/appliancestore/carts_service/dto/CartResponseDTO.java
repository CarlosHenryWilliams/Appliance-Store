package com.appliancestore.carts_service.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CartResponseDTO {
    private Long idCart;
    private List<ItemResponseDTO> items;
    private Double totalPrice;
}
