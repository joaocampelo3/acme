package com.isep.acme.model.DTO;

import java.time.LocalDate;
import java.util.UUID;

public class ReviewDTO {

    private Long idReview;
    private UUID reviewUuid;
    private String reviewText;
    private LocalDate publishingDate;
    private String approvalStatus;
    private String funFact;
    private Double rating;

    public ReviewDTO(Long idReview, UUID reviewUuid, String reviewText, LocalDate publishingDate, String approvalStatus, String funFact, Double rating) {
        this.idReview = idReview;
        this.reviewUuid = reviewUuid;
        this.reviewText = reviewText;
        this.publishingDate = publishingDate;
        this.approvalStatus = approvalStatus;
        this.funFact = funFact;
        this.rating = rating;
    }

    public void setIdReview( Long idReview ) {
        this.idReview = idReview;
    }

    public Long getIdReview() {
        return this.idReview;
    }

    public UUID getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
