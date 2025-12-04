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


   // @CircuitBreaker(name="carts-service", fallbackMethod="fallbackFindCartById")
   /* public CartDTO findCartById(Long idCart){
        return cartIntegration.findCartById(idCart); // throw cart wasn't found through feign.
    }*/
    /*public CartDTO fallbackFindCartById(Long idCart){
        throw new CartServiceUnavailableException("Cart Service Unavailable, Cart not found");
    }*/

    @Override
    //@CircuitBreaker(name="carts-service", fallbackMethod="fallbackCreateSale")
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

    public void fallbackCreateSale(SaleRequestDTO saleRequestDTO, Throwable t) {
        throw new CartServiceUnavailableException("Cart Service Unavailable");
    }

    private SaleResponseDTO aggregateSaleToDTO(Sale sale) {
        // Call to CARTAPI To get products
        CartDTO cart = cartIntegration.findCartById(sale.getIdCart());
        return saleMapper.mapSaletoSaleResponseDTO(sale, cart.getProductDetailsResponseDTOList());
    }

    @Override
    @CircuitBreaker(name="carts-service", fallbackMethod="fallbackGetSales")
    public List<SaleResponseDTO> findAllSales() {
        return saleRepo.findAll().stream().map(this::aggregateSaleToDTO).toList();
    }

    @Override
    @CircuitBreaker(name="carts-service", fallbackMethod="fallbackGetSale")
    public SaleResponseDTO findSaleById(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        return this.aggregateSaleToDTO(sale);
    }

    // FallBack Methods
    public SaleResponseDTO fallbackGetSale(Throwable throwable){
        return new SaleResponseDTO(0L,null,0D,0L,null);
    }

    public List<SaleResponseDTO> fallbackGetSales(Throwable throwable){
        List<SaleResponseDTO> emptyList = new ArrayList<>();
        SaleResponseDTO defaultSaleResponse = new SaleResponseDTO(0L,null,0D,0L,null);
        emptyList.add(defaultSaleResponse);
        return emptyList;
    }

    public void createException(){
        throw new IllegalArgumentException("Prueba Resillience y circuit breaker");
    }


    @Override
    public SaleResponseDTO updateSale(Long idSale, SaleRequestDTO saleRequestDTO) {
        Sale saleToUpdate = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        // old Cart
        CartDTO oldCartDTO = cartIntegration.findCartById(saleToUpdate.getIdCart());
        // new Cart
        CartDTO newCartDTO = cartIntegration.findCartById(saleRequestDTO.getIdCart());

        // old Cart, add the quantity to a product (Inventory Reversal)
        productIntegration.addProductQuantity(oldCartDTO);

        // subtract a quantity to a product.
        productIntegration.subtractProductQuantity(newCartDTO);

        saleToUpdate.setIdCart(newCartDTO.getIdCart());
        saleToUpdate.setTotalPrice(newCartDTO.getTotalPrice());

        saleRepo.save(saleToUpdate);
        return this.aggregateSaleToDTO(saleToUpdate);
    }

    @Override
    public void deleteSale(Long idSale) {
        Sale sale = saleRepo.findById(idSale).orElseThrow(() -> new SaleNotFoundException("The sale with the ID: " + idSale + " wasn't found."));
        CartDTO cart = cartIntegration.findCartById(sale.getIdCart()); // throw cart wasn't found through feign.

        // Add quantity to products  (Old sale) Reversal Inventory.
        productIntegration.addProductQuantity(cart);
        saleRepo.deleteById(idSale);
    }




}
