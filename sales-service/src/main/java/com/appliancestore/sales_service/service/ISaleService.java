package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.dto.SaleCreateDTO;
import com.appliancestore.sales_service.dto.SaleResponseDTO;
import com.appliancestore.sales_service.model.Sale;

import java.util.List;

public interface ISaleService {
    /*CRUD Methods */

    // Create
    public void createSale(SaleCreateDTO saleCreateDTO);

    // Read
    public List<SaleResponseDTO> findAllSales();

    public SaleResponseDTO findSaleById(Long idSale);

    // Update
    public Sale updateSale(Long idSale, Sale sale);

    // Delete
    public void deleteSale(Long idSale);
}
