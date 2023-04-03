package com.isep.acme.model;

import com.isep.acme.model.DTO.VoteReviewDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class VoteTemp {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long voteID;


    @Id
    @Column(nullable = false)
    private Long voteTempID;

    @Column(nullable = false)
    private String vote;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private String review;

    protected VoteTemp() {

    }

    public VoteTemp(Long voteID, Long voteTempID, String vote, Long userID, String review) {
        this.voteID = voteID;
        this.voteTempID = voteTempID;
        this.vote = vote;
        this.userID = userID;
        this.review = review;
    }

    public Long getVoteID() {
        return voteID;
    }

    public void setVoteID(Long voteID) {
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
        return Objects.equals(voteID, vote1.voteID) && Objects.equals(vote, vote1.vote)
                && Objects.equals(userID, vote1.userID) && Objects.equals(review, vote1.review);
    }

    public void updateVote(VoteTemp v) {
        setVote(v.vote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteID, vote, userID);
    }

    public Long getVoteTempID() {
        return voteTempID;
    }

    public void setVoteTempID(Long voteTempID) {
        this.voteTempID = voteTempID;
    }

    public VoteReviewDTO toDto() {
        return new VoteReviewDTO(this.voteID, this.voteTempID, this.userID, this.vote, this.review);
    }
}
