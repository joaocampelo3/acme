package com.isep.acme.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Document(collection = "review")
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEvent {

    @Id
    private Long idReview;
    private String reviewUuid;
    private long version;
    private String approvalStatus;
    private String reviewText;
    private String report;
    private String publishingDate;
    private String funFact;
    private String sku;
    private Long userId;
    private Double rating;
    private Integer upVotes;
    private Integer downVotes;
    private EventTypeEnum eventTypeEnum;

    public ReviewEvent(final Long idReview, final String reviewUuid, final long version, final String approvalStatus, final String reviewText,
                       final String publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public ReviewEvent(final Long idReview, final String reviewUuid, final long version, final String approvalStatus, final  String reviewText,
                       final String report, final String publishingDate, final String funFact, String sku, Double rating,
                       Long userId) {
        this(idReview, reviewUuid, version, approvalStatus, reviewText, publishingDate, funFact);
        setReport(report);
        setSku(sku);
        setRating(rating);
        setUserId(userId);

    }

    public Long getIdReview() {
        return idReview;
    }

    public String getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(String reviewUuid) {
        this.reviewUuid = reviewUuid;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Boolean setApprovalStatus(String approvalStatus) {

        if( approvalStatus.equalsIgnoreCase("pending") ||
            approvalStatus.equalsIgnoreCase("approved") ||
            approvalStatus.equalsIgnoreCase("rejected")) {
            
            this.approvalStatus = approvalStatus;
            return true;
        }
        return false;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        if (reviewText == null || reviewText.isBlank()) {
            throw new IllegalArgumentException("Review Text is a mandatory attribute of Review.");
        }
        if (reviewText.length() > 2048) {
            throw new IllegalArgumentException("Review Text must not be greater than 2048 characters.");
        }

        this.reviewText = reviewText;
    }

    public void setReport(String report) {
        if (report.length() > 2048) {
            throw new IllegalArgumentException("Report must not be greater than 2048 characters.");
        }
        this.report = report;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    public long getVersion() {
        return version;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public static ReviewEvent fromJson(String json) {
        return new GsonBuilder().serializeNulls().create().fromJson(json, ReviewEvent.class);
    }

    public String toJson() {
        return new GsonBuilder().serializeNulls().create().toJson(this);
    }
}
