package com.isep.acme.events;

import com.google.gson.Gson;

import java.util.Optional;
import java.util.UUID;

public class ReviewEvent {
    private Long reviewId;

    private UUID voteTempID;

    public ReviewEvent(Long reviewId, UUID voteTempID) {
        this.reviewId = reviewId;
        this.voteTempID = voteTempID;
    }

    public ReviewEvent(Long reviewId) {
        this.reviewId = reviewId;
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ReviewEvent fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ReviewEvent.class);
    }
}
