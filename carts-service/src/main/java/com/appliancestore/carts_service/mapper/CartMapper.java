package com.appliancestore.carts_service.mapper;

import com.appliancestore.carts_service.dto.CartRequestDTO;
import com.appliancestore.carts_service.dto.CartResponseDTO;
import com.appliancestore.carts_service.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {

    // CartRequestDTO to Cart
    public Cart mapCartRequestDtoToCart(CartRequestDTO cartRequestDTO);

    // Cart to CartResponseDTO
    public CartResponseDTO mapCartToCartResponseDto(Cart cart);
    // update a Cart from a cartRequestDTO
    public void updateACartFromCartRequestDto(CartRequestDTO cartRequestDTO, @MappingTarget Cart cart);
}
