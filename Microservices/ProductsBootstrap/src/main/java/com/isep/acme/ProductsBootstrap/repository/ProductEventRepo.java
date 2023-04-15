package com.isep.acme.ProductsBootstrap.repository;

import com.isep.acme.ProductsBootstrap.model.ProductEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductEventRepo extends MongoRepository<ProductEvent, Long> {
    List<ProductEvent> findByProductID(Long productID);

    List<ProductEvent> findAll();
}