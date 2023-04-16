package com.isep.acme.ProductsBootstrap.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isep.acme.ProductsBootstrap.model.dto.ProductEventDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Data
@Document(collection = "product")
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent implements Serializable {

    public String sku;
    @Id
    private Long productID;
    private String designation;
    private String description;
    private EventTypeEnum eventTypeEnum;
    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    public ProductEvent(final Long productID, final String sku) {
        this.productID = Objects.requireNonNull(productID);
        setSku(sku);
    }

    public ProductEvent(final String sku) {
        setSku(sku);
    }

    public ProductEvent(final String sku, final String designation, final String description) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation is a mandatory attribute of Product.");
        }
        if (designation.length() > 50) {
            throw new IllegalArgumentException("Designation must not be greater than 50 characters.");
        }
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Product.");
        }

        if (description.length() > 1200) {
            throw new IllegalArgumentException("Description must not be greater than 1200 characters.");
        }

        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }
        if (sku.length() != 12) {
            throw new IllegalArgumentException("SKU must be 12 characters long.");
        }

        this.sku = sku;
    }

    public void updateProduct(ProductEvent p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public Long getProductID() {
        return productID;
    }

    public ProductEventDTO toDto() {
        return new ProductEventDTO(this.sku, this.designation);
    }

    public EventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public static ProductEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, ProductEvent.class);
    }
    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

}
