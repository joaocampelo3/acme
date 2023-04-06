package com.isep.acme.model;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @Column(nullable = false, unique = true)
    public String sku;

    public Product() {
    }

    public Product(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
