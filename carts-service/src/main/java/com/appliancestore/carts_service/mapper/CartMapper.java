package com.appliancestore.carts_service.mapper;

import com.appliancestore.carts_service.dto.CartRequestDTO;
import com.appliancestore.carts_service.dto.CartResponseDTO;
import com.appliancestore.carts_service.dto.ProductDTO;
import com.appliancestore.carts_service.dto.ProductDetailsResponseDTO;
import com.appliancestore.carts_service.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    // CartRequestDTO to Cart
    public Cart mapCartRequestDtoToCart(CartRequestDTO cartRequestDTO);

    // Cart to CartResponseDTO
    // public CartResponseDTO mapCartToCartResponseDTO(Cart cart); OLD
    public CartResponseDTO mapCartToCartResponseDTO(Cart cart, List<ProductDetailsResponseDTO> productDetailsResponseDTOList);

    /// I use "default" because I'm working in an interface.
    public default ProductDetailsResponseDTO mapProductToProductDetailsResponseDTO(ProductDTO productDTO, int quantity){
        return new ProductDetailsResponseDTO(productDTO.getIdProduct(), productDTO.getName(), productDTO.getBrand(), productDTO.getPrice(), quantity);
    }

    // update a Cart from a cartRequestDTO
    public void updateACartFromCartRequestDTO(CartRequestDTO cartRequestDTO, @MappingTarget Cart cart);
}
