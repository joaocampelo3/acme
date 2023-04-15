package com.isep.acme.loadbalancer.model.DTO;


import java.util.UUID;

public class VoteTempDTO {
    private Long userID;
    private String vote;
    private String review;

    public VoteTempDTO(UUID voteTempID, Long userID, String vote, String review) {
        this.vote = vote;
        this.review = review;
        this.userID = userID;
    }



    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

}