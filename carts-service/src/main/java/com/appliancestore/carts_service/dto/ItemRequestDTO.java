package com.appliancestore.carts_service.dto;

import com.appliancestore.carts_service.model.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ItemRequestDTO {
    private Long idProduct;
    private int quantity;
}
