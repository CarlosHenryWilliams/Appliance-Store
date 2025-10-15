package com.appliancestore.sales_service.service;

import com.appliancestore.sales_service.dto.CartDTO;
import com.appliancestore.sales_service.dto.InventoryUpdateDTO;
import com.appliancestore.sales_service.dto.SaleCreateDTO;
import com.appliancestore.sales_service.model.Sale;
import com.appliancestore.sales_service.repository.ICartAPI;
import com.appliancestore.sales_service.repository.IProductAPI;
import com.appliancestore.sales_service.repository.ISaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleService implements ISaleService{

    @Autowired
    private ISaleRepository saleRepo;

    @Autowired
    private ICartAPI cartAPI;

    @Autowired
    private IProductAPI productAPI;

    @Override
    public void createSale(SaleCreateDTO saleCreateDTO) {
        CartDTO cart = cartAPI.findCartById(saleCreateDTO.getIdCart()); // throw cart doesnt found.
        Sale saleToCreate = new Sale();
        saleToCreate.setIdCart(cart.getIdCart()); // assign a cart to the sale
        System.out.println("TotalPrice" + cart.getTotalPrice());
        saleToCreate.setTotalPrice(cart.getTotalPrice());
        saleToCreate.setSaleDate(LocalDate.now()); // Current date.

        System.out.println(cart.getProductDTOList());
        for(InventoryUpdateDTO inven : cart.getProductDTOList()){
            productAPI.subtractProductQuantity(new InventoryUpdateDTO(inven.getIdProduct(), inven.getQuantity()));
        }

        saleRepo.save(saleToCreate);
    }

    @Override
    public List<Sale> findAllSales() {
        return saleRepo.findAll();
    }

    @Override
    public Sale findSaleById(Long idSale) {
        return saleRepo.findById(idSale).orElse(null);
    }

    @Override
    public Sale updateSale(Long idSale, Sale sale) {
        return null;
    }

    @Override
    public void deleteSale(Long idSale) {
        saleRepo.deleteById(idSale);
    }


}
