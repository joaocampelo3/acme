package com.isep.acme.ProductsBootstrap.services.impl;

import com.isep.acme.ProductsBootstrap.model.DTO.ProductDTO;
import com.isep.acme.ProductsBootstrap.model.Product;
import com.isep.acme.ProductsBootstrap.repository.ProductRepository;
import com.isep.acme.ProductsBootstrap.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<String> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<String> message = null;
        ProductDTO productDTO;
        for (Product product : productList) {
            productDTO = new ProductDTO(product.getSku(), product.getDesignation());
            message.add(productDTO.toJson().toString());
        }
        return message;
    }

    @Override
    public Product createProduct(Product product) {
        if (checkProductExists(product.getProductID())){
            throw new RuntimeException("Product already exists");
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        if (!checkProductExists(product.getProductID())){
            throw new RuntimeException("Product do not exists");
        }
        productRepository.deleteByProductID(product.getProductID());
    }

    @Override
    public Product updateProduct(Product product) {
        if (checkProductExists(product.getProductID())){
            throw new RuntimeException("Product already exists");
        }

        Product existingProduct = productRepository.findByID(product.getProductID());
        existingProduct.setSku(product.getSku());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setDesignation(product.getDesignation());

        return productRepository.save(existingProduct);
    }

    @Override
    public Boolean checkProductExists(Long id) {
        return productRepository.existsById(id);
    }
}
