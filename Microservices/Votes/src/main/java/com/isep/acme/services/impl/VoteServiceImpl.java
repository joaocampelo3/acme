package com.isep.acme.services.impl;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.services.interfaces.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository repository;

    @Override
    public Optional<VoteReviewDTO> findByVoteID(Long voteID) {
        return Optional.empty();
    }

    @Override
    public VoteReviewDTO create(Vote vote) throws Exception {
        final Vote v = new Vote(vote.getVoteID(), vote.getVoteTempID(), vote.getVote(), vote.getUserID());

        VoteReviewDTO voteReviewDTO = repository.save(v).toDto();

        Publisher.main("Vote Created");

        return voteReviewDTO;
    }

    @Override
    public VoteReviewDTO updateByVoteID(Long voteID, Vote vote) {
        return null;
    }

    @Override
    public void deleteByVoteID(Long vote) {

    }

    @Override
    public List<VoteReviewDTO> getAll() {
        Iterable<Vote> v = repository.findAll();
        List<VoteReviewDTO> vDto = new ArrayList();
        for (Vote vote:v) {
            vDto.add(vote.toDto());
        }
        return vDto;
    }
}
