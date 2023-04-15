package com.isep.acme.loadbalancer.model;

import com.isep.acme.loadbalancer.model.DTO.VoteDTO;

import java.util.Objects;
import java.util.UUID;

public class Vote {

    private UUID voteID;

    private String vote;

    private Long userID;

    private Long reviewID;

    public Vote(UUID voteID, String vote, Long userID, Long reviewID) {
        this.voteID = voteID;
        this.vote = vote;
        this.userID = userID;
        this.reviewID = reviewID;
    }

    public Vote(String vote, Long userID, Long reviewID) {
        this.vote = vote;
        this.userID = userID;
        this.reviewID = reviewID;
    }

    protected Vote() {

    }
    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }


    public Vote(UUID voteID) {
        this.voteID = voteID;
    }

    public UUID getVoteID() {
        return voteID;
    }

    public void setVoteID(UUID voteID) {
        this.voteID = voteID;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote1 = (Vote) o;
        return Objects.equals(voteID, vote1.voteID) && Objects.equals(vote, vote1.vote) && Objects.equals(userID, vote1.userID);
    }

    public void updateVote(Vote v) {
        setVote(v.vote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteID, vote, userID);
    }

    public VoteDTO toDto() {
        return new VoteDTO(this.voteID, this.userID, this.vote);
    }
}
