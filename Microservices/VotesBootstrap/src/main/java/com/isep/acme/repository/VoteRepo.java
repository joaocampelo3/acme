package com.isep.acme.repository;

import com.isep.acme.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepo extends MongoRepository<Vote, String> {
    Vote findByID(Long id);

    List<Vote> findAll();

    void deleteByVoteID(Long idVote);

    boolean existsById(Long id);
}