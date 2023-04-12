package com.isep.acme.ProductsBootstrap.repository;

import com.isep.acme.ProductsBootstrap.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByID(Long id);

    List<Product> findAll();

    Product save(Product product);

    void deleteByProductID(Long productId);

    boolean existsById(Long id);
}