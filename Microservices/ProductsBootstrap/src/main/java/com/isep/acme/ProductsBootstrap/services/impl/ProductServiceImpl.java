package com.isep.acme.ProductsBootstrap.services.impl;

import com.isep.acme.ProductsBootstrap.model.Product;
import com.isep.acme.ProductsBootstrap.repository.ProductRepo;
import com.isep.acme.ProductsBootstrap.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        if (checkProductExists(product.getProductID())) {
            throw new RuntimeException("Product already exists");
        }
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        if (!checkProductExists(product.getProductID())) {
            throw new RuntimeException("Product do not exists");
        }
        productRepo.deleteByProductID(product.getProductID());
    }

    @Override
    public Product updateProduct(Product product) {
        if (checkProductExists(product.getProductID())) {
            throw new RuntimeException("Product already exists");
        }

        Product existingProduct = productRepo.findByID(product.getProductID());
        existingProduct.setSku(product.getSku());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setDesignation(product.getDesignation());

        return productRepo.save(existingProduct);
    }

    @Override
    public Boolean checkProductExists(Long id) {
        return productRepo.existsById(id);
    }
}