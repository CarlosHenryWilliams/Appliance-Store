package com.appliancestore.carts_service.service;

import com.appliancestore.carts_service.dto.CartRequestDTO;
import com.appliancestore.carts_service.dto.CartResponseDTO;


import java.util.List;

public interface ICartService {
    /*CRUD Methods
     * */

    // Create
    public void createCart(CartRequestDTO cartRequestDTO);

    // Read
    public List<CartResponseDTO> findAllCarts();
    public CartResponseDTO findCartById(Long idCart);

    // Update
    public  CartResponseDTO updateCart(Long idCart, CartRequestDTO cartRequestDTO);

    // Delete
    public void deleteCart(Long idCart);
}
