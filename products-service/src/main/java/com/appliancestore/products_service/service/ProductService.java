package com.appliancestore.products_service.service;

import com.appliancestore.products_service.dto.InventoryUpdateDTO;
import com.appliancestore.products_service.dto.ProductRequestDTO;
import com.appliancestore.products_service.dto.ProductResponseDTO;
import com.appliancestore.products_service.exception.InsufficientStockException;
import com.appliancestore.products_service.exception.ProductNotFoundException;
import com.appliancestore.products_service.mapper.ProductMapper;
import com.appliancestore.products_service.model.Product;
import com.appliancestore.products_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {


    @Autowired
    private IProductRepository produRepo;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.mapRequestDtoToProduct(productRequestDTO);
        System.out.println("Stock from DTO: "+ productRequestDTO.getStock());
        System.out.println("Stock from product after mappper: "+ product.getStock());

        produRepo.save(product);
    }

    @Override
    public List<ProductResponseDTO> findAllProducts() {
        // "map" a product
        return produRepo.findAll().stream().map(productMapper::mapProductToDtoResponse).toList();
    }

    @Override
    public ProductResponseDTO findProductById(Long idProduct) {
        Product product = produRepo.findById(idProduct).orElseThrow(() -> new ProductNotFoundException("The product with the ID: " + idProduct + " wasn't found."));
        return productMapper.mapProductToDtoResponse(product);
    }

    @Override
    public List<ProductResponseDTO> findProductsByIds(List<Long> idProductsList) {
        return produRepo.findAllById(idProductsList).stream().map(productMapper::mapProductToDtoResponse).toList();
    }

    @Override
    public ProductResponseDTO updateProduct(Long idProduct, ProductRequestDTO productRequestDTO) {

        // Find the product
        Product productToUpdate = produRepo.findById(idProduct)
                .orElseThrow(() -> new ProductNotFoundException("The product with the ID: " + idProduct + " wasn't found."));

        // Update product through MapStruct, from a productRequestDTO to a Product
        productMapper.updateProductFromDto(productRequestDTO, productToUpdate);
        // Save entity
        Product updatedProduct = produRepo.save(productToUpdate);
        return productMapper.mapProductToDtoResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long idProduct) {
        this.findProductById(idProduct);
        produRepo.deleteById(idProduct);
    }

    @Override
    public void subtractProductQuantity(InventoryUpdateDTO inventoryUpdateDTO) {
      Product productToUpdate =  produRepo.findById(inventoryUpdateDTO.getIdProduct())
              .orElseThrow(() -> new ProductNotFoundException("The product with the ID: " + inventoryUpdateDTO.getIdProduct() + " wasn't found."));
      if(productToUpdate.getStock() < inventoryUpdateDTO.getQuantity()){
          throw new InsufficientStockException("The product with the ID: " + inventoryUpdateDTO.getIdProduct() + " has insufficient stock.");
      }
      productToUpdate.setStock(productToUpdate.getStock() - inventoryUpdateDTO.getQuantity());
      produRepo.save(productToUpdate);
    }

    @Override
    public void addProductQuantity(InventoryUpdateDTO inventoryUpdateDTO) {
        Product productToUpdate =  produRepo.findById(inventoryUpdateDTO.getIdProduct())
                .orElseThrow(() -> new ProductNotFoundException("The product with the ID: " + inventoryUpdateDTO.getIdProduct() + " wasn't found."));
        productToUpdate.setStock(productToUpdate.getStock() + inventoryUpdateDTO.getQuantity());
        produRepo.save(productToUpdate);
    }
}
