package com.isep.acme.model;

import com.google.gson.Gson;
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
public class VoteEvent {
    @Id
    private UUID voteID;
    private Long reviewID;
    private String reviewText;
    private String sku;
    private String vote;
    private Long userID;
    private EventTypeEnum eventTypeEnum;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteEvent voteEvent1 = (VoteEvent) o;
        return Objects.equals(voteID, voteEvent1.voteID) && Objects.equals(vote, voteEvent1.vote) && Objects.equals(userID, voteEvent1.userID);
    }

    public void updateVote(VoteEvent v) {
        setVote(v.vote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteID, vote, userID);
    }

    public static VoteEvent fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, VoteEvent.class);
    }
}
