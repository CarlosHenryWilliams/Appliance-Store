package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.dto.*;
import com.appliancestore.sales_service.exception.SaleNotFoundException;
import com.appliancestore.sales_service.mapper.SaleMapper;
import com.appliancestore.sales_service.model.Sale;
import com.appliancestore.sales_service.repository.ICartAPI;
import com.appliancestore.sales_service.repository.IProductAPI;
import com.appliancestore.sales_service.repository.ISaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository saleRepo;

    @Autowired
    private ICartAPI cartAPI;

    @Autowired
    private IProductAPI productAPI;

    @Autowired
    private SaleMapper saleMapper;

    @Override
    public void createSale(SaleCreateDTO saleCreateDTO) {

        CartDTO cart = cartAPI.findCartById(saleCreateDTO.getIdCart()); // throw cart wasn't found through feign.


        for (ProductDTO product : cart.getProductDetailsResponseDTOList()) {
            // subtract a quantity to a product
            productAPI.subtractProductQuantity(new InventoryUpdateDTO(product.getIdProduct(), product.getQuantity()));
        }

        Sale saleToCreate = new Sale();
        saleToCreate.setIdCart(cart.getIdCart()); // assign a cart to the sale
        saleToCreate.setTotalPrice(cart.getTotalPrice());
        saleToCreate.setSaleDate(LocalDate.now()); // Current date.

        saleRepo.save(saleToCreate);
    }


    private SaleResponseDTO aggregateSaleToDTO(Sale sale) {
        // Call to CARTAPI To get products
        CartDTO cart = cartAPI.findCartById(sale.getIdCart());
        return saleMapper.mapSaletoSaleResponseDTO(sale, cart.getProductDetailsResponseDTOList());
    }

    @Override
    public List<SaleResponseDTO> findAllSales() {
        return saleRepo.findAll().stream().map(this::aggregateSaleToDTO).toList();
    }

    @Override
    public SaleResponseDTO findSaleById(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + "wasn't found."));
        return this.aggregateSaleToDTO(sale);
    }

    @Override
    public Sale updateSale(Long idSale, Sale sale) {
        return null;
    }

    @Override
    public void deleteSale(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        CartDTO cart = cartAPI.findCartById(sale.getIdCart()); // throw cart wasn't found through feign.

        for (ProductDTO productDTO : cart.getProductDetailsResponseDTOList()) {
            productAPI.addProductQuantity(new InventoryUpdateDTO(productDTO.getIdProduct(),productDTO.getQuantity()));
        }
        saleRepo.deleteById(idSale);
    }


}
