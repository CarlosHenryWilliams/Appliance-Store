package com.appliancestore.sales_service.mapper;

import com.appliancestore.sales_service.dto.ProductDTO;
import com.appliancestore.sales_service.dto.SaleResponseDTO;
import com.appliancestore.sales_service.model.Sale;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    //Sale to SaleResponseDTO
    public SaleResponseDTO mapSaletoSaleResponseDTO(Sale sale, List<ProductDTO> productDetailsResponseDTOList);

}
