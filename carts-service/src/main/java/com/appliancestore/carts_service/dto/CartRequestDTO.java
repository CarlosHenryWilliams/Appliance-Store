package com.appliancestore.carts_service.dto;

import com.appliancestore.carts_service.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class CartRequestDTO {
    private List<ItemRequestDTO> items;
}
