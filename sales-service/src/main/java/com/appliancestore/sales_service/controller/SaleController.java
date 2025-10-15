package com.appliancestore.sales_service.controller;

import com.appliancestore.sales_service.dto.SaleCreateDTO;
import com.appliancestore.sales_service.model.Sale;
import com.appliancestore.sales_service.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/sales")
public class SaleController {
    @Autowired
    private ISaleService saleServ;

    @PostMapping()
    public ResponseEntity<String> createSale(@RequestBody SaleCreateDTO saleCreateDTO) {
        saleServ.createSale(saleCreateDTO);
        return new ResponseEntity<String>("The sale has been created", HttpStatus.CREATED);
    }

    // Read
    @GetMapping()
    public ResponseEntity<List<Sale>> findAllSales() {
        return new ResponseEntity<List<Sale>>(saleServ.findAllSales(), HttpStatus.OK);
    }

    @GetMapping("/{idSale}")
    public ResponseEntity<Sale> findSaleById(@PathVariable Long idSale) {
        return new ResponseEntity<Sale>(saleServ.findSaleById(idSale), HttpStatus.OK);
    }

    // Update
    @PutMapping("/{idSale}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long idSale, @RequestBody Sale sale) {
        return new ResponseEntity<Sale>(saleServ.updateSale(idSale, sale), HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{idSale}")
    public ResponseEntity<String> deleteCart(@PathVariable Long idSale) {
        saleServ.deleteSale(idSale);
        return new ResponseEntity<String>("The sale with the ID:" + idSale + "has been deleted.", HttpStatus.OK);
    }
}
