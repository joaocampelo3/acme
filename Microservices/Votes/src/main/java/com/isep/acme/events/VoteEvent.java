package com.isep.acme.events;

import com.google.gson.GsonBuilder;
import com.isep.acme.model.Vote;

import java.util.UUID;

public class VoteEvent {
    private UUID voteUuid;
    private String vote;

    private Long reviewID;

    private String reviewText;

    private String sku;

    private  Long userID;
    private EventTypeEnum eventTypeEnum;

    public Long getReviewID() {
        return reviewID;
    }

    public VoteEvent(UUID voteUuid) {
        this.voteUuid = voteUuid;
    }

    public VoteEvent(UUID voteUuid, Long reviewID) {
        this.voteUuid = voteUuid;
        this.reviewID = reviewID;
    }

    public VoteEvent(UUID voteUuid, String vote) {
        this.voteUuid = voteUuid;
        this.vote = vote;
    }
    public VoteEvent(UUID voteUuid, String vote, Long reviewID, Long userID) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.reviewID = reviewID;
        this.userID = userID;
    }

    public VoteEvent(UUID voteUuid, String vote, String reviewText, String sku, Long userID) {
        this.voteUuid = voteUuid;
        this.reviewText = reviewText;
        this.sku = sku;
        this.userID = userID;
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

    public UUID getVoteUuid() {
        return voteUuid;
    }

    public void setVoteUuid(UUID voteUuid) {
        this.voteUuid = voteUuid;
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
        return new Vote(this.voteUuid, this.vote, this.userID, this.reviewID);
    }
}
