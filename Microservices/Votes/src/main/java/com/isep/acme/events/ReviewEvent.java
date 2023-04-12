package com.isep.acme.events;

import com.google.gson.Gson;

public class ReviewEvent {
    private Long reviewId;

    public ReviewEvent(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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
