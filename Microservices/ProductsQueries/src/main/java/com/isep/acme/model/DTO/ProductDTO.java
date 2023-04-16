package com.isep.acme.model.DTO;

public class ProductDTO {
    private String sku;
    private String designation;
    private String description;

    public ProductDTO(String sku, String designation, String description) {
        this.sku = sku;
        this.designation = designation;
        this.description = description;
    }

    public ProductDTO() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
