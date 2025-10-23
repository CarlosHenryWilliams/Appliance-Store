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
    public void createSale(SaleRequestDTO saleRequestDTO) {

        CartDTO cart = cartAPI.findCartById(saleRequestDTO.getIdCart()); // throw cart wasn't found through feign.


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
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        return this.aggregateSaleToDTO(sale);
    }

    @Override
    public SaleResponseDTO updateSale(Long idSale, SaleRequestDTO saleRequestDTO) {
        Sale saleToUpdate = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));

        // old Cart
        CartDTO oldCartDTO = cartAPI.findCartById(saleToUpdate.getIdCart());
        // new Cart
        CartDTO newCartDTO = cartAPI.findCartById(saleRequestDTO.getIdCart());

        // old Cart I add the quantity to a product (Inventory Reversal)
        for(ProductDTO product : oldCartDTO.getProductDetailsResponseDTOList()){
            productAPI.addProductQuantity(new InventoryUpdateDTO(product.getIdProduct(), product.getQuantity()));
        }

        for(ProductDTO product : newCartDTO.getProductDetailsResponseDTOList()){
            productAPI.subtractProductQuantity(new InventoryUpdateDTO(product.getIdProduct(), product.getQuantity()));
        }

        saleToUpdate.setIdCart(newCartDTO.getIdCart());
        saleToUpdate.setTotalPrice(newCartDTO.getTotalPrice());

        saleRepo.save(saleToUpdate);
        return this.aggregateSaleToDTO(saleToUpdate);
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
