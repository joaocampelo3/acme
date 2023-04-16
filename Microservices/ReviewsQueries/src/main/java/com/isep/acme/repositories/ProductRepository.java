package com.isep.acme.repositories;

import com.isep.acme.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    //Obtain the catalog of products -> Catalog: show sku and designation of all products
    @Query("SELECT p FROM Product p WHERE p.sku=:sku")
    Optional<Product> getProductBySku(String sku);
}

