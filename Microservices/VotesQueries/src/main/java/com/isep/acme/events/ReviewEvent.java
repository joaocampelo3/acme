package com.isep.acme.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Optional;
import java.util.UUID;

public class ReviewEvent {
    private Long reviewId;

    private UUID reviewUuid;
    private UUID voteTempID;
    private EventTypeEnum eventTypeEnum;

    public ReviewEvent(Long reviewId, UUID voteTempID, EventTypeEnum eventTypeEnum) {
        this.reviewId = reviewId;
        this.voteTempID = voteTempID;
        this.eventTypeEnum = eventTypeEnum;
    }

    public UUID getReviewUuid() {
        return reviewUuid;
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

    public EventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(EventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }

    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }

    public static ReviewEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, ReviewEvent.class);
    }
}
