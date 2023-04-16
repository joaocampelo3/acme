package com.isep.acme.events;

import com.google.gson.GsonBuilder;
import com.isep.acme.model.Rating;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class ReviewEvent {
    private Long idReview;
    private String reviewUuid;
    private Long version;
    private String approvalStatus;
    private String reviewText;
    private String publishingDate;
    private String funFact;
    private String sku;
    private Long userId;
    private String comment;
    private Double rating;
    private UUID voteTempID;
    private EventTypeEnum eventTypeEnum;

    public ReviewEvent(Long idReview, EventTypeEnum eventTypeEnum) {
        this.idReview = idReview;
        this.eventTypeEnum = eventTypeEnum;
    }

    public ReviewEvent(Long idReview, UUID voteTempID) {
        this.idReview = idReview;
        this.voteTempID = voteTempID;
    }

    public ReviewEvent(Long idReview, String reviewUuid, String sku, Long userId, String comment, Double rating, EventTypeEnum eventTypeEnum) {
        this.idReview = idReview;
        this.sku = sku;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
        this.eventTypeEnum = eventTypeEnum;
    }

    public ReviewEvent(Long idReview, String reviewUuid, Long version, String approvalStatus, String reviewText, String publishingDate, String funFact, String sku, Long userId, String comment, Double rating) {
        this.idReview = idReview;
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.sku = sku;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
    }

    public ReviewEvent(Long idReview, String reviewUuid, Long version, String approvalStatus, String reviewText, String publishingDate, String funFact, String sku, Long userId, String comment, Double rating, EventTypeEnum eventTypeEnum) {
        this.idReview = idReview;
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.sku = sku;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
        this.eventTypeEnum = eventTypeEnum;
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public String getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(String reviewUuid) {
        this.reviewUuid = reviewUuid;
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

    public Review toReview(User user){
        Review review = new Review(this.idReview, UUID.fromString(this.reviewUuid), this.version, this.approvalStatus, this.reviewText, LocalDate.parse(this.publishingDate), this.funFact);
        review.setSku(this.sku);
        review.setRating(new Rating(this.rating));
        review.setUser(user);
        return review;
    }
}
