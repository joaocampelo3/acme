package com.isep.acme.model;


import javax.persistence.*;

@Entity
public class AggregatedRating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aggregatedId;

    @Column()
    private double average;

    @Column()
    private Long productId;

    @Column()
    private String sku;

    protected AggregatedRating() {
    }

    public AggregatedRating(double average, Long productId, String sku) {
        this.average = average;
        this.productId = productId;
        this.sku = sku;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getAggregatedId() {
        return aggregatedId;
    }
}
