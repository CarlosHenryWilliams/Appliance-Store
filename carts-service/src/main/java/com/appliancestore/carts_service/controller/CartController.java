package com.appliancestore.carts_service.controller;

import com.appliancestore.carts_service.dto.CartRequestDTO;
import com.appliancestore.carts_service.dto.CartResponseDTO;
import com.appliancestore.carts_service.model.Cart;
import com.appliancestore.carts_service.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private ICartService cartService;

    // Create
    @PostMapping()
    public ResponseEntity<String> createCart(@RequestBody CartRequestDTO cartRequestDTO) {
        cartService.createCart(cartRequestDTO);
        return new ResponseEntity<String>("The cart has been created", HttpStatus.CREATED);
    }

    // Read
    @GetMapping()
    public ResponseEntity<List<CartResponseDTO>> findAllCarts() {
        return new ResponseEntity<List<CartResponseDTO>>(cartService.findAllCarts(), HttpStatus.OK);
    }

    @GetMapping("/{idCart}")
    public ResponseEntity<CartResponseDTO> findCartById(@PathVariable Long idCart) {
        return new ResponseEntity<CartResponseDTO>(cartService.findCartById(idCart), HttpStatus.OK);
    }

    // Update
    @PutMapping("/{idCart}")
    public ResponseEntity<CartResponseDTO> updateCart(@PathVariable Long idCart, @RequestBody CartRequestDTO cartRequestDTO) {
        return new ResponseEntity<CartResponseDTO>(cartService.updateCart(idCart, cartRequestDTO), HttpStatus.OK);
    }
    // Delete
    @DeleteMapping("/{idCart}")
    public ResponseEntity<String> deleteCart(@PathVariable Long idCart) {
        cartService.deleteCart(idCart);
        return new ResponseEntity<String>("The cart with the ID " + idCart + " has been deleted", HttpStatus.OK);
    }
}
