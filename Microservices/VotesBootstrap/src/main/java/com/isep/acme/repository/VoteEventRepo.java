package com.isep.acme.repository;

import com.isep.acme.model.VoteEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteEventRepo extends MongoRepository<VoteEvent, UUID> {
    List<VoteEvent> findByVoteID(UUID voteID);

    List<VoteEvent> findAll();
}