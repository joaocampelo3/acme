package com.isep.acme.loadbalancer.model.DTO;

import java.util.UUID;

public class CreateReviewDTO {

    private String reviewText;

    private Long userID;

    private Double rating;

    private UUID voteID;

    public CreateReviewDTO(){}

    public CreateReviewDTO(String reviewText, Long userID, UUID voteID) {
        this.reviewText = reviewText;
        this.userID = userID;
        this.voteID = voteID;
    }

    public CreateReviewDTO(String reviewText, Long userID) {
        this.reviewText = reviewText;
        this.userID = userID;
    }

    public CreateReviewDTO(Double rating) {
        this.rating = rating;
    }


    public UUID getVoteID() {
        return voteID;
    }

    public void setVoteID(UUID voteID) {
        this.voteID = voteID;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
