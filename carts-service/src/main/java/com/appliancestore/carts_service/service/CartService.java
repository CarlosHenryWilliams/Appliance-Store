package com.appliancestore.carts_service.service;

import com.appliancestore.carts_service.dto.*;
import com.appliancestore.carts_service.exception.CartNotFoundException;
import com.appliancestore.carts_service.exception.InsufficientProductStockException;
import com.appliancestore.carts_service.exception.ProductNotFoundException;
import com.appliancestore.carts_service.mapper.CartMapper;
import com.appliancestore.carts_service.model.Cart;
import com.appliancestore.carts_service.model.Item;
import com.appliancestore.carts_service.repository.ICartRepository;
import com.appliancestore.carts_service.repository.IProductAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    @Autowired
    private ICartRepository cartRepo;

    @Autowired
    private IProductAPI prodAPI;

    @Autowired
    private CartMapper cartMapper;


    @Override
    public void createCart(CartRequestDTO cartRequestDTO) {
        // Map a CartRequestDto to a Cart
        Cart cart = cartMapper.mapCartRequestDtoToCart(cartRequestDTO);
        List<Long> productsCartIds = cart.getItems().stream().map(Item::getIdProduct).toList();

        // Products API
        List<ProductDTO> allProducts = prodAPI.findAllProductsByIds(productsCartIds);

        // Create a map with a key (ID) and a new DTO ProductInfoDTO with the price and the stock.
        Map<Long, ProductInfoDTO> productsDetailsMap = allProducts.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getIdProduct, // Product DTO because it has the id.
                        p -> new ProductInfoDTO(p.getPrice(), p.getStock())
                ));
       double totalPrice = 0D;

        // Foreach for items.
        for (Item item : cart.getItems()) {
            if (!productsDetailsMap.containsKey(item.getIdProduct())) { // if the key doesn't exist in the map
                throw new ProductNotFoundException("The product with the ID:" + item.getIdProduct() + " wasn't found.");
            }

            item.setCart(cart); // assign a cart to each item
            ProductInfoDTO productInfoDTOMap = productsDetailsMap.get(item.getIdProduct()); // get the stock and price with a key (id)
            if (productInfoDTOMap.getStock() < item.getQuantity()) { // check sufficient stock.
                throw new InsufficientProductStockException("The product with the ID: " + item.getIdProduct() + " has insufficient stock.");
            }
            double productPrice = productInfoDTOMap.getPrice();// get value of the key
            totalPrice += item.getQuantity() * productPrice;
        }
        cart.setTotalPrice(totalPrice);
        cartRepo.save(cart);
    }

    private CartResponseDTO aggregateCartToDTO(Cart cart) {
        List<ProductDetailsResponseDTO> productDetailsResponseDTOList = new ArrayList<>();
        for(Item item: cart.getItems()){
            // Call Products API
            ProductDTO productDTO = prodAPI.findProductById(item.getIdProduct());
            // Merge Product details to the productDetailsResponseDTO with CartMapper.
            // ADD to the list
            productDetailsResponseDTOList.add(cartMapper.mapProductToProductDetailsResponseDTO(productDTO, item.getQuantity()));
        }
        return cartMapper.mapCartToCartResponseDTO(cart, productDetailsResponseDTOList);
    }

    @Override
    public List<CartResponseDTO> findAllCarts() {
        return cartRepo.findAll().stream().map(this::aggregateCartToDTO).toList();
    }

    @Override
    public CartResponseDTO findCartById(Long idCart) {
        Cart cart = cartRepo.findById(idCart).orElseThrow(() -> new CartNotFoundException("The cart with the ID:" + idCart + " wasn't found."));
        return  aggregateCartToDTO(cart);
    }

    @Override
    public CartResponseDTO updateCart(Long idCart, CartRequestDTO cartRequestDTO) {

        // Find a cart to edit
        Cart cartToEdit = cartRepo.findById(idCart).orElseThrow(() -> new CartNotFoundException("The cart with the ID:" + idCart + " wasn't found."));

        cartToEdit.getItems().clear(); // Remove items before adding new ones.

        // update a Cart from a CartRequestDTO
        cartMapper.updateACartFromCartRequestDTO(cartRequestDTO, cartToEdit);

        List<Long> productsCartIds = cartToEdit.getItems().stream().map(Item::getIdProduct).toList();

        // Products API
        List<ProductDTO> allProductsAPI = prodAPI.findAllProductsByIds(productsCartIds);

        // Create a map with a key (ID) and a new DTO ProductInfoDTO with the price and the stock.
        Map<Long, ProductInfoDTO> productDetailsMap = allProductsAPI.stream()
                .collect(Collectors.toMap(
                        ProductDTO::getIdProduct,
                        p -> new ProductInfoDTO(p.getPrice(), p.getStock())
                ));

        double totalPrice = 0D;
        // Foreach for items.
        for (Item item : cartToEdit.getItems()) {
            item.setCart(cartToEdit); // assign a cart to each item
            if (!productDetailsMap.containsKey(item.getIdProduct())) { // if the key doesn't exist in the map
                throw new ProductNotFoundException("The product with the ID:" + item.getIdProduct() + " wasn't found");
            }
            ProductInfoDTO productInfoDTOMap = productDetailsMap.get(item.getIdProduct()); // get the stock and price with a key (id) from a map (productDetailsMap)
            if (productInfoDTOMap.getStock() < item.getQuantity()) { // check sufficient stock.
                throw new InsufficientProductStockException("The product with the ID: " + item.getIdProduct() + " has insufficient stock.");
            }
            double productPrice = productInfoDTOMap.getPrice();// get value of the key
            totalPrice += item.getQuantity() * productPrice;
        }
        cartToEdit.setTotalPrice(totalPrice);
        cartRepo.save(cartToEdit); // Save changes.

        // Map a cart to a CartResponseDTO
        return  aggregateCartToDTO(cartToEdit);
    }

    @Override
    public void deleteCart(Long idCart) {
        CartResponseDTO cartResponseDTO = this.findCartById(idCart);
        cartRepo.deleteById(cartResponseDTO.getIdCart());
    }

}
