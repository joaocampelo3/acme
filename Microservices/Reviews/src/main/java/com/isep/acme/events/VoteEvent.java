package com.isep.acme.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

public class VoteEvent {
    private UUID voteID;

    private Long reviewID;

    private String reviewText;

    private String sku;

    private Long userID;

    public Long getReviewID() {
        return reviewID;
    }

    public VoteEvent(UUID voteID, Long reviewID) {
        this.voteID = voteID;
        this.reviewID = reviewID;
    }
    public VoteEvent(UUID voteID, String reviewText, String sku, Long userID) {
        this.voteID = voteID;
        this.reviewText = reviewText;
        this.sku = sku;
        this.userID = userID;
    }


    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }


    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }


    public VoteEvent(UUID voteID) {
        this.voteID = voteID;
    }

    public UUID getVoteID() {
        return voteID;
    }

    public void setVoteID(UUID voteID) {
        this.voteID = voteID;
    }

    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    public static VoteEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, VoteEvent.class);
    }
}
