package com.isep.acme.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isep.acme.model.Product;

public class ProductEvent {
    private String sku;

    private String designation;
    private String description;
    private EventTypeEnum eventTypeEnum;

    public ProductEvent(String sku, String designation) {
        this.sku = sku;
        this.designation = designation;
    }

    public ProductEvent(String sku, String designation, EventTypeEnum eventTypeEnum) {
        this.sku = sku;
        this.designation = designation;
        this.eventTypeEnum = eventTypeEnum;
    }

    public ProductEvent(String sku, String designation, String description) {
        this.sku = sku;
        this.designation = designation;
        this.description = description;
    }

    public ProductEvent(String sku, String designation, String description, EventTypeEnum eventTypeEnum) {
        this.sku = sku;
        this.designation = designation;
        this.description = description;
        this.eventTypeEnum = eventTypeEnum;
    }

    public ProductEvent(String sku, EventTypeEnum eventTypeEnum) {
        this.sku = sku;
        this.eventTypeEnum = eventTypeEnum;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public EventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    public static ProductEvent fromJson(String json) {
        Gson gson = new Gson();
        return new GsonBuilder().serializeNulls().create().fromJson(json, ProductEvent.class);
    }

    public Product toProduct() {
        return new Product(this.getSku(),this.designation, this.description);
    }
}
