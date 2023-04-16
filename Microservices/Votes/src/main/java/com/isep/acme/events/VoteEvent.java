package com.isep.acme.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isep.acme.model.Vote;

import java.util.UUID;

public class VoteEvent {
    private UUID voteID;
    private String vote;

    private Long reviewID;

    private String reviewText;

    private String sku;

    private  Long userID;
    private EventTypeEnum eventTypeEnum;

    public Long getReviewID() {
        return reviewID;
    }

    public VoteEvent(UUID voteID) {
        this.voteID = voteID;
    }

    public VoteEvent(UUID voteID, Long reviewID) {
        this.voteID = voteID;
        this.reviewID = reviewID;
    }
    public VoteEvent(UUID voteID, EventTypeEnum eventTypeEnum) {
        this.voteID = voteID;
        this.eventTypeEnum = eventTypeEnum;
    }
    public VoteEvent(UUID voteID, String vote) {
        this.voteID = voteID;
        this.vote = vote;
    }
    public VoteEvent(UUID voteID, String vote, Long reviewID, Long userID) {
        this.voteID = voteID;
        this.vote = vote;
        this.reviewID = reviewID;
        this.userID = userID;
    }

    public VoteEvent(UUID voteID, String vote, EventTypeEnum eventTypeEnum) {
        this.voteID = voteID;
        this.vote = vote;
        this.eventTypeEnum = eventTypeEnum;
    }
    public VoteEvent(UUID voteID, String vote, String reviewText, String sku, Long userID) {
        this.voteID = voteID;
        this.reviewText = reviewText;
        this.sku = sku;
        this.userID = userID;
    }
    public VoteEvent(UUID voteID, String reviewText, String sku, Long userID, EventTypeEnum eventTypeEnum) {
        this.voteID = voteID;
        this.reviewText = reviewText;
        this.sku = sku;
        this.userID = userID;
        this.eventTypeEnum = eventTypeEnum;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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

    public UUID getVoteID() {
        return voteID;
    }

    public void setVoteID(UUID voteID) {
        this.voteID = voteID;
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

    public static VoteEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, VoteEvent.class);
    }

    public Vote toVote(){
        return new Vote(this.voteID, this.vote, this.userID, this.reviewID);
    }
}
