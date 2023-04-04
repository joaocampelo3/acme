package com.isep.acme.model.DTO;

import com.google.gson.Gson;

import java.util.UUID;

public class VoteDTO {

    private Long id;
    private UUID voteID;
    private Long userID;
    private String vote;

    public VoteDTO(Long id, UUID voteID, Long userID, String vote) {
        this.id = id;
        this.voteID = voteID;
        this.userID = userID;
        this.vote = vote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVoteID(UUID voteID) {
        this.voteID = voteID;
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

    public static VoteDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, VoteDTO.class);
    }
}