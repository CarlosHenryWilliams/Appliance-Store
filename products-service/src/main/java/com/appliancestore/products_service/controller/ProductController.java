package com.appliancestore.products_service.controller;

import com.appliancestore.products_service.dto.ProductRequestDTO;
import com.appliancestore.products_service.dto.ProductResponseDTO;
import com.appliancestore.products_service.exception.ProductNotFoundException;
import com.appliancestore.products_service.model.Product;
import com.appliancestore.products_service.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private IProductService produServ;

    // Create
    @PostMapping()
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestDTO productRequestDTO){
        produServ.createProduct(productRequestDTO);
        return new ResponseEntity<String>("The product has been created", HttpStatus.CREATED);
    }

    // Read
    @GetMapping()
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts(){
        return new ResponseEntity<List<ProductResponseDTO>>(produServ.findAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/{idProduct}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable  Long idProduct){
        return new ResponseEntity<ProductResponseDTO>(produServ.findProductById(idProduct),HttpStatus.OK);
    }

    // Update
    @PutMapping("/{idProduct}")
    public ResponseEntity<ProductResponseDTO>  updateProduct(@PathVariable Long idProduct, @RequestBody ProductRequestDTO productRequestDTO){
        return new ResponseEntity<ProductResponseDTO>(produServ.updateProduct(idProduct, productRequestDTO),HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{idProduct}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long idProduct){
        produServ.deleteProduct(idProduct);
        return new ResponseEntity<String>("The product with the ID:" + idProduct + " has been deleted",HttpStatus.OK);
    }

    @PatchMapping("/{idProduct}/{quantity}")
    public ResponseEntity<String> substractProductQuantity(@PathVariable  Long idProduct, @PathVariable int quantity) {
        produServ.substractProductQuantity(idProduct, quantity);
        return new ResponseEntity<String>("The product with the ID:" + idProduct + " has been updated",HttpStatus.OK);

    }
}
