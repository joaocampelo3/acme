package com.isep.acme.loadbalancer.model.DTO;

import java.util.UUID;

public class VoteDTO {

    private Long userID;
    private String vote;

    public VoteDTO(UUID voteID, Long userID, String vote) {
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

}