package com.isep.acme.model;

import com.isep.acme.model.DTO.VoteTempDTO;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class VoteTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID voteTempID;

    @Column(nullable = false)
    private String vote;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private String review;

    protected VoteTemp() {

    }

    public VoteTemp(String vote, Long userID, String review) {
        this.voteTempID = voteTempID;
        this.vote = vote;
        this.userID = userID;
        this.review = review;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteTemp vote1 = (VoteTemp) o;
        return Objects.equals(vote, vote1.vote)
                && Objects.equals(userID, vote1.userID) && Objects.equals(review, vote1.review);
    }

    public void updateVote(VoteTemp v) {
        setVote(v.vote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vote, userID);
    }

    public UUID getVoteTempID() {
        return voteTempID;
    }

    public void setVoteTempID(UUID voteTempID) {
        this.voteTempID = voteTempID;
    }

    public VoteTempDTO toDto() {
        return new VoteTempDTO(this.voteTempID, this.userID, this.vote, this.review);
    }
}
