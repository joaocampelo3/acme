package com.isep.acme.events;

import com.google.gson.GsonBuilder;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;

import java.util.UUID;

public class VoteEvent {
    private UUID voteUuid;
    private String vote;

    private UUID reviewUuid;

    private String reviewText;

    private String sku;

    private Long userID;
    private EventTypeEnum eventTypeEnum;

    public UUID getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
    }

    public VoteEvent(UUID voteUuid) {
        this.voteUuid = voteUuid;
    }

    public VoteEvent(UUID voteUuid, EventTypeEnum eventTypeEnum) {
        this.voteUuid = voteUuid;
        this.eventTypeEnum = eventTypeEnum;
    }

    public VoteEvent(UUID voteUuid, UUID reviewUuid) {
        this.voteUuid = voteUuid;
        this.reviewUuid = reviewUuid;
    }

    public VoteEvent(UUID voteUuid, String vote, EventTypeEnum eventTypeEnum) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.eventTypeEnum = eventTypeEnum;
    }
    public VoteEvent(UUID voteUuid, String vote, UUID reviewUuid, Long userID, EventTypeEnum eventTypeEnum) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.reviewUuid = reviewUuid;
        this.userID = userID;
        this.eventTypeEnum = eventTypeEnum;
    }

    public VoteEvent(UUID voteUuid, String vote, String reviewText, String sku, Long userID) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.reviewText = reviewText;
        this.sku = sku;
        this.userID = userID;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
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


    public void setreviewUuid(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
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

    public Vote toVote(User user){
        Vote v = new Vote(this.voteUuid, this.vote, this.userID, this.reviewUuid);
        v.setUserID(user.getUserId());
        return v;
    }
}
