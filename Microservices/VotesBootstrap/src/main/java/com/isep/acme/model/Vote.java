package com.isep.acme.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Document(collection = "vote")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vote {
    @Id
    private UUID voteID;
    private Long id;
    private String vote;
    private Long userID;


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
}
