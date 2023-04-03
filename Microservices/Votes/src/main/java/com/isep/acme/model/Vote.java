package com.isep.acme.model;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.DTO.VoteReviewDTO;

import javax.persistence.*;

import java.util.Objects;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long voteID;

    @Column(nullable = false)
    private String vote;

    @Column(nullable = false)
    private Long userID;

    protected Vote() {

    }

    public Vote(Long voteID, String vote, Long userID) {
        this.voteID = voteID;
        this.vote = vote;
        this.userID = userID;
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
