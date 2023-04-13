package com.isep.acme.events;

import com.google.gson.Gson;

public class ReviewEvent {
    private Long reviewId;
    private String sku;
    private String userId;
    private String comment;
    private int rating;

    public ReviewEvent(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
