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
    private UUID reviewUuid;

    public Vote(UUID voteUuid, String vote, Long userID, UUID reviewUuid) {
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.userID = userID;
        this.reviewUuid = reviewUuid;
    }

    public Vote(Long voteID, UUID voteUuid, String vote, Long userID, UUID reviewUuid) {
        this.voteID = voteID;
        this.voteUuid = voteUuid;
        this.vote = vote;
        this.userID = userID;
        this.reviewUuid = reviewUuid;
    }

    public Vote(String vote, Long userID, UUID reviewUuid) {
        setVoteUuid(UUID.randomUUID());
        this.vote = vote;
        this.userID = userID;
        this.reviewUuid = reviewUuid;
    }

    protected Vote() {

    }

    public Long getVoteID() {
        return voteID;
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

    public UUID getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
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
