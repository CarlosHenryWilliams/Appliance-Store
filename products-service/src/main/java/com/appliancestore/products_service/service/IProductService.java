package com.appliancestore.products_service.service;

import com.appliancestore.products_service.dto.InventoryUpdateDTO;
import com.appliancestore.products_service.dto.ProductRequestDTO;
import com.appliancestore.products_service.dto.ProductResponseDTO;

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

    // Subtract a quantity
    public void subtractProductQuantity(InventoryUpdateDTO inventoryUpdateDTO);

}
