package com.isep.acme.model.DTO;

public class VoteReviewDTO {

    private Long voteID;

    public Long getVoteTempID() {
        return voteTempID;
    }

    public void setVoteTempID(Long voteTempID) {
        this.voteTempID = voteTempID;
    }

    private Long voteTempID;
    private Long userID;
    private String vote;


    public VoteReviewDTO(Long voteID, Long voteTempID, Long userID, String vote) {
        this.voteID = voteID;
        this.voteTempID = voteTempID;
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
}