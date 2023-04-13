package com.isep.acme.ProductsBootstrap.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.isep.acme.ProductsBootstrap.model.Product;

import java.util.List;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {
    Product findByID(Long id);

    List<Product> findAll();

    void deleteByProductID(Long productId);

    boolean existsById(Long id);
}