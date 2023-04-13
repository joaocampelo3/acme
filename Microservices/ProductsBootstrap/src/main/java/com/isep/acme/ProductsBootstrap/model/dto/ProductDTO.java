package com.isep.acme.ProductsBootstrap.model.dto;

import com.google.gson.Gson;

public class ProductDTO {
    private String sku;
    private String designation;

    public ProductDTO(String sku, String designation) {
        this.sku = sku;
        this.designation = designation;
    }

    public static ProductDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProductDTO.class);
    }

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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
