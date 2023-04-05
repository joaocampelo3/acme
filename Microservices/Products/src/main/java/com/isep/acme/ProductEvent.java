package com.isep.acme;

import com.google.gson.Gson;

public class ProductEvent {
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ProductEvent fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProductEvent.class);
    }
}
