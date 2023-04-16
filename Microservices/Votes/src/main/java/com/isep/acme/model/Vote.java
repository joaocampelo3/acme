package com.isep.acme.model;

import com.isep.acme.model.DTO.VoteDTO;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long voteID;

    @Column(nullable = false)
    private UUID voteUuid;

    @Column(nullable = false)
    private String vote;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long reviewID;

    public Vote(UUID voteUuid, String vote, Long userID, Long reviewID) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.userID = userID;
        this.reviewID = reviewID;
    }

    public Vote(Long voteID, UUID voteUuid, String vote, Long userID, Long reviewID) {
        this.voteID = voteID;
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.userID = userID;
        this.reviewID = reviewID;
    }

    public Vote(String vote, Long userID, Long reviewID) {
        setVoteUuid(UUID.randomUUID());
        this.vote = vote;
        this.userID = userID;
        this.reviewID = reviewID;
    }

    protected Vote() {

    }

    public Long getVoteID() {
        return voteID;
    }

    public Long getReviewID() {
        return reviewID;
    }

    public void setReviewID(Long reviewID) {
        this.reviewID = reviewID;
    }

    public UUID getVoteUuid() {
        return voteUuid;
    }

    public void setVoteUuid(UUID voteUuid) {
        this.voteUuid = voteUuid;
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
        return new VoteDTO(this.voteUuid.toString(), this.userID, this.vote);
    }
}
