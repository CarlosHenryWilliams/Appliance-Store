package com.appliancestore.products_service.service;

import com.appliancestore.products_service.dto.ProductRequestDTO;
import com.appliancestore.products_service.dto.ProductResponseDTO;
import com.appliancestore.products_service.model.Product;

import java.util.List;

public interface IProductService {
    /*CRUD Methods
    * */

    // Create
    public void createProduct(ProductRequestDTO productRequestDTO);

    // Read
    public List<ProductResponseDTO> findAllProducts();
    public ProductResponseDTO findProductById(Long idProduct);

    // Update
    public  ProductResponseDTO updateProduct(Long idProduct, ProductRequestDTO productRequestDTO);

    // Delete
    public void deleteProduct(Long idProduct);

    // Substract a quantity
    public void substractProductQuantity(Long idProduct, int quantity);

}
