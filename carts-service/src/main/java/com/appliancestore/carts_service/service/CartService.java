package com.appliancestore.carts_service.service;

import com.appliancestore.carts_service.dto.*;
import com.appliancestore.carts_service.exception.CartNotFoundException;
import com.appliancestore.carts_service.exception.InsufficientProductStockException;
import com.appliancestore.carts_service.exception.ProductNotFoundException;
import com.appliancestore.carts_service.mapper.CartMapper;
import com.appliancestore.carts_service.model.Cart;
import com.appliancestore.carts_service.model.Item;
import com.appliancestore.carts_service.repository.ICartRepository;
import com.appliancestore.carts_service.repository.IProductsAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepo;

    @Autowired
    private IProductsAPI prodAPI;

    @Autowired
    private CartMapper cartMapper;


    @Override
    public void createCart(CartRequestDTO cartRequestDTO) {
        double totalPrice = 0D;
        // Products API
        List<ProductDTO> allProducts = prodAPI.findAllProductsAPI();
        // Create a map with a key (ID) and a new DTO ProductInfoDTO with the price and the stock.
        Map<Long, ProductInfoDTO> productsPriceMap = allProducts.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getIdProduct,
                        p -> new ProductInfoDTO(p.getPrice(), p.getStock())
                ));

        // Map a CartRequestDto to a Cart
        Cart cart = cartMapper.mapCartRequestDtoToCart(cartRequestDTO);

        // Foreach for items.
        for (Item item : cart.getItems()) {
            if (!productsPriceMap.containsKey(item.getIdProduct())) { // if the key doesn't exist in the map
                throw new ProductNotFoundException("The product with the ID:" + item.getIdProduct() + " wasn't found.");
            }

            item.setCart(cart); // assign a cart to each item
            ProductInfoDTO productInfoDTOMap = productsPriceMap.get(item.getIdProduct()); // get the stock and price with a key (id)
            if (productInfoDTOMap.getStock() < item.getQuantity()) { // check sufficient stock.
                throw new InsufficientProductStockException("The product with the ID: " + item.getIdProduct() + " has insufficient stock.");
            }
            double productPrice = productInfoDTOMap.getPrice();// get value of the key
            totalPrice += item.getQuantity() * productPrice;
        }
        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
    }

    @Override
    public List<CartResponseDTO> findAllCarts() {
        return cartRepo.findAll().stream().map(cartMapper::mapCartToCartResponseDto).toList();
    }

    @Override
    public CartResponseDTO findCartById(Long idCart) {
        Cart cart = cartRepo.findById(idCart).orElseThrow(() -> new CartNotFoundException("The cart with the ID:" + idCart + " wasn't found."));
        return cartMapper.mapCartToCartResponseDto(cart);
    }

    @Override
    public CartResponseDTO updateCart(Long idCart, CartRequestDTO cartRequestDTO) {
        Cart cartToEdit = cartRepo.findById(idCart).orElseThrow(() -> new CartNotFoundException("The cart with the ID:" + idCart + " wasn't found."));
        double totalPrice = 0D;

        // Products API
        List<ProductDTO> allProducts = prodAPI.findAllProductsAPI();
        // Create a map with a key (ID) and a new DTO ProductInfoDTO with the price and the stock.
        Map<Long, ProductInfoDTO> productsPriceMap = allProducts.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getIdProduct,
                        p -> new ProductInfoDTO(p.getPrice(), p.getStock())
                ));

        cartToEdit.getItems().clear(); // Remove items before adding new ones.

        // update a Cart from a CarRequestDTO
        cartMapper.updateACartFromCartRequestDto(cartRequestDTO, cartToEdit);

        // Foreach for items.
        for (Item item : cartToEdit.getItems()) {
            item.setCart(cartToEdit); // assign a cart to each item
            if (!productsPriceMap.containsKey(item.getIdProduct())) { // if the key doesn't exist in the map
                throw new ProductNotFoundException("The product with the ID:" +item.getIdProduct() + " wasn't found");
            }
            ProductInfoDTO productInfoDTOMap = productsPriceMap.get(item.getIdProduct()); // get the stock and price with a key (id) from a map (productsPriceMap)
            if (productInfoDTOMap.getStock() < item.getQuantity()) { // check sufficient stock.
                throw new InsufficientProductStockException("The product with the ID: " + item.getIdProduct() + " has insufficient stock.");
            }
            double productPrice = productInfoDTOMap.getPrice();// get value of the key
            totalPrice += item.getQuantity() * productPrice;
        }
        cartToEdit.setTotalPrice(totalPrice);
        cartRepo.save(cartToEdit); // Save changes.

        // Map a cart to a CartResponseDTO
        return  cartMapper.mapCartToCartResponseDto(cartToEdit);
    }

    @Override
    public void deleteCart(Long idCart) {
        CartResponseDTO cartResponseDTO = this.findCartById(idCart);
        cartRepo.deleteById(cartResponseDTO.getIdCart());
    }

}
