package com.appliancestore.carts_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;
    private Long idProduct;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "idCart")
    @JsonIgnore
    private Cart cart;
}
