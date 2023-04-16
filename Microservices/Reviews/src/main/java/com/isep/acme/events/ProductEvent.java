package com.isep.acme.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProductEvent {
    private String sku;

    public ProductEvent(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    public static ProductEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, ProductEvent.class);
    }
}
