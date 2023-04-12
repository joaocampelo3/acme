package com.isep.acme.ProductsBootstrap.services.interfaces;

import com.isep.acme.ProductsBootstrap.model.Product;

import java.util.List;

public interface ProductService {
    List<String> getAllProducts();

    Product createProduct(Product product);

    void deleteProduct(Product product);

    Product updateProduct(Product product);

    Boolean checkProductExists(Long id);
}
