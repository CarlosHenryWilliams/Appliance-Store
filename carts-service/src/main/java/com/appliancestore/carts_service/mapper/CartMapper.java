package com.appliancestore.carts_service.mapper;

import com.appliancestore.carts_service.dto.CartRequestDTO;
import com.appliancestore.carts_service.dto.CartResponseDTO;
import com.appliancestore.carts_service.dto.ProductDTO;
import com.appliancestore.carts_service.dto.ProductDetailsResponseDTO;
import com.appliancestore.carts_service.model.Cart;
import com.appliancestore.carts_service.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    // CartRequestDTO to Cart
    public Cart mapCartRequestDtoToCart(CartRequestDTO cartRequestDTO);

    // Cart to CartResponseDTO
    // public CartResponseDTO mapCartToCartResponseDTO(Cart cart); OLD
    public CartResponseDTO mapCartToCartResponseDTO(Cart cart, List<ProductDetailsResponseDTO> productDetailsResponseDTOList);


    // update a Cart from a cartRequestDTO
    public void updateACartFromCartRequestDTO(CartRequestDTO cartRequestDTO, @MappingTarget Cart cart);

    /// I use "default" because I'm working in an interface.
    public default ProductDetailsResponseDTO mapProductToProductDetailsResponseDTO(ProductDTO productDTO, int quantity){
        return new ProductDetailsResponseDTO(productDTO.getIdProduct(), productDTO.getName(), productDTO.getBrand(), productDTO.getPrice(), quantity);
    }

    public default List<ProductDetailsResponseDTO> mapProductsToProductDetailsResponseDTOList(
            List<ProductDTO> products,
            List<Item> items) {

        // Create a map with the productId and the Quantity.
        Map<Long, Integer> quantityMap = items.stream()
                .collect(Collectors.toMap(Item::getIdProduct, Item::getQuantity));

        //  product to ProductDetailsResponseDTO
        return products.stream()
                .map(product -> mapProductToProductDetailsResponseDTO(
                        product,
                        quantityMap.get(product.getIdProduct())
                ))
                .toList();
    }


}
