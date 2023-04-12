package com.isep.acme.model.DTO;

import com.google.gson.Gson;

import java.util.UUID;

public class VoteDTO {

    private Long userID;
    private String vote;

    public VoteDTO(UUID voteID, Long userID, String vote) {
        this.userID = userID;
        this.vote = vote;
    }

    public static VoteDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, VoteDTO.class);
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}