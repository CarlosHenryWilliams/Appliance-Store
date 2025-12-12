package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.dto.*;
import com.appliancestore.sales_service.exception.CartServiceUnavailableException;
import com.appliancestore.sales_service.exception.ProductServiceUnavailableException;
import com.appliancestore.sales_service.exception.SaleNotFoundException;
import com.appliancestore.sales_service.mapper.SaleMapper;
import com.appliancestore.sales_service.model.Sale;
import com.appliancestore.sales_service.repository.ICartAPI;
import com.appliancestore.sales_service.repository.IProductAPI;
import com.appliancestore.sales_service.repository.ISaleRepository;
import com.appliancestore.sales_service.service.integration.CartIntegrationService;
import com.appliancestore.sales_service.service.integration.ProductIntegrationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository saleRepo;

    @Autowired
    private CartIntegrationService cartIntegration;

    @Autowired
    private ProductIntegrationService productIntegration;

    @Autowired
    private SaleMapper saleMapper;


    @Override
    public void createSale(SaleRequestDTO saleRequestDTO) {
        CartDTO cart = cartIntegration.findCartById(saleRequestDTO.getIdCart());
       //CartDTO cart = cartAPI.findCartById(saleRequestDTO.getIdCart()); // throw cart wasn't found through feign.

        // Subtract a quantity to a product.
        productIntegration.subtractProductQuantity(cart);

        Sale saleToCreate = new Sale();
        saleToCreate.setIdCart(cart.getIdCart()); // assign a cart to the sale
        saleToCreate.setTotalPrice(cart.getTotalPrice());
        saleToCreate.setSaleDate(LocalDate.now()); // Current date.

        saleRepo.save(saleToCreate);
    }


    @Override
    public List<SaleResponseDTO> findAllSales() {
        return saleRepo.findAll().stream().map(this::aggregateSaleToDTO).toList();
    }

    @Override
    public SaleResponseDTO findSaleById(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        return this.aggregateSaleToDTO(sale);
    }

    @Override
    public SaleResponseDTO updateSale(Long idSale, SaleRequestDTO saleRequestDTO) {
        // A sale cannot be updated.
        return null;
    }

    @Override
    public void deleteSale(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        CartDTO cart = cartIntegration.findCartById(sale.getIdCart()); // throw cart wasn't found through feign.

        // Add quantity to products  (Old sale) Reversal Inventory (Product Service).
        productIntegration.addProductQuantity(cart);
        saleRepo.deleteById(idSale);
    }

    private SaleResponseDTO aggregateSaleToDTO(Sale sale) {
        // Call to CARTAPI To get products
        CartDTO cart = cartIntegration.findCartById(sale.getIdCart());
        return saleMapper.mapSaletoSaleResponseDTO(sale, cart.getProductDetailsResponseDTOList());
    }

}
