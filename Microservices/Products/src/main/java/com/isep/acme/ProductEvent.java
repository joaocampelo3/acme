package com.isep.acme;

import com.google.gson.Gson;
import com.isep.acme.model.Product;

public class ProductEvent {
    private String sku;

    private String designation;
    private String description;
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ProductEvent fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProductEvent.class);
    }

    public Product toProduct() {
        return new Product(this.getSku(),this.designation, this.description);
    }
}
