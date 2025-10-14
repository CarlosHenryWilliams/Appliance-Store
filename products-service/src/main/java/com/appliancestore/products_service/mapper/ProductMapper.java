package com.appliancestore.products_service.mapper;

import com.appliancestore.products_service.dto.ProductRequestDTO;
import com.appliancestore.products_service.dto.ProductResponseDTO;
import com.appliancestore.products_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // ProductRequestDTO To Product
    Product mapRequestDtoToProduct(ProductRequestDTO productRequestDTO);

    // Product to ProductResponseDTO
    ProductResponseDTO mapProductToDtoResponse(Product product);
    // ProductResponeDTO to Product
    Product mapResponseDtoToProduct(ProductResponseDTO productToEdit);
    //Update a product mapper from a product
    public void updateProductFromDto(ProductRequestDTO productRequestDTO, @MappingTarget Product product);

}
