package com.isep.acme.model.DTO;

import com.google.gson.Gson;

public class VoteReviewDTO {

    private Long voteID;
    private Long voteTempID;
    private Long userID;
    private String vote;

    private String review;

    public VoteReviewDTO(Long voteID, Long voteTempID, Long userID, String vote, String review) {
        this.voteID = voteID;
        this.voteTempID = voteTempID;
        this.userID = userID;
        this.vote = vote;
        this.review = review;
    }

    public static VoteReviewDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, VoteReviewDTO.class);
    }

    public Long getVoteTempID() {
        return voteTempID;
    }

    public void setVoteTempID(Long voteTempID) {
        this.voteTempID = voteTempID;
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

    public Long getVoteID() {
        return voteID;
    }

    public void setVoteID(Long voteID) {
        this.voteID = voteID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}