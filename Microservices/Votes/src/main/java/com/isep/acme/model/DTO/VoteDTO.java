package com.isep.acme.model.DTO;

import com.google.gson.Gson;

public class VoteDTO {

    private Long voteID;
    private Long userID;
    private String vote;

    public VoteDTO(Long voteID, Long userID, String vote) {
        this.voteID = voteID;
        this.userID = userID;
        this.vote = vote;
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static VoteDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, VoteDTO.class);
    }
}