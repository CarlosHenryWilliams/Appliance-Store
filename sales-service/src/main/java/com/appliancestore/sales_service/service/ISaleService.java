package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.dto.SaleRequestDTO;
import com.appliancestore.sales_service.dto.SaleResponseDTO;
import com.appliancestore.sales_service.model.Sale;

import java.util.List;

public interface ISaleService {
    /*CRUD Methods */

    // Create
    public void createSale(SaleRequestDTO saleRequestDTO);

    // Read
    public List<SaleResponseDTO> findAllSales();

    public SaleResponseDTO findSaleById(Long idSale);

    // Update
    public SaleResponseDTO updateSale(Long idSale, SaleRequestDTO saleRequestDTO);

    // Delete
    public void deleteSale(Long idSale);
}
