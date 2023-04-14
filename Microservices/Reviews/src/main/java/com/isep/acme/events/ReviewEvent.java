package com.isep.acme.events;

import com.google.gson.Gson;

import java.util.UUID;

public class ReviewEvent {
    private Long reviewId;
    private String sku;
    private Long userId;
    private String comment;
    private Double rating;

    private UUID voteTempID;

    public ReviewEvent(Long reviewId) {
        this.reviewId = reviewId;
    }

    public ReviewEvent(Long reviewId, String sku, Long userId, String comment, Double rating) {
        this.reviewId = reviewId;
        this.sku = sku;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public ReviewEvent(Long reviewId, UUID voteTempID) {
        this.reviewId = reviewId;
        this.voteTempID = voteTempID;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public UUID getVoteTempID() {
        return voteTempID;
    }

    public void setVoteTempID(UUID voteTempID) {
        this.voteTempID = voteTempID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ReviewEvent fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ReviewEvent.class);
    }
}
