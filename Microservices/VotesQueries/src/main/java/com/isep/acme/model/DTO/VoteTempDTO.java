package com.isep.acme.model.DTO;

import com.google.gson.Gson;

import java.util.UUID;

public class VoteTempDTO {
    private String voteTempUuid;
    private Long userID;
    private String vote;
    private String review;

    public VoteTempDTO(String voteTempUuid, Long userID, String vote, String review) {
        this.voteTempUuid = voteTempUuid;
        this.vote = vote;
        this.review = review;
        this.userID = userID;
    }

    public String getVoteTempUuid() {
        return voteTempUuid;
    }

    public void setVoteTempUuid(String voteTempUuid) {
        this.voteTempUuid = voteTempUuid;
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