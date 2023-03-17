package com.isep.acme.services.impl;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.services.interfaces.VoteService;
import com.isep.acme.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class VoteServiceImpl implements VoteService {


    @Override
    public Optional<VoteReviewDTO> findByVoteID(Long voteID) {
        return Optional.empty();
    }

    @Override
    public VoteReviewDTO create(Vote vote) {
        return null;
    }

    @Override
    public VoteReviewDTO updateByVoteID(Long voteID, Vote vote) {
        return null;
    }

    @Override
    public void deleteByVoteID(Long vote) {

    }
}
