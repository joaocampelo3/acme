package com.isep.acme.ProductsBootstrap.model.dto;

import com.google.gson.Gson;

public class ProductEventDTO {
    private String sku;
    private String designation;

    public ProductEventDTO(String sku, String designation) {
        this.sku = sku;
        this.designation = designation;
    }

    public static ProductEventDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProductEventDTO.class);
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
