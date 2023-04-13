package com.isep.acme.services.impl;

import com.isep.acme.model.Vote;
import com.isep.acme.repository.VoteRepo;
import com.isep.acme.services.interfaces.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class VoteServiceImpl implements VoteService {

    private VoteRepo voteRepo;

    @Override
    public List<Vote> getAllVotes() {
        return voteRepo.findAll();
    }

    @Override
    public Vote createVote(Vote vote) {
        if (checkVoteExists(vote.getId())) {
            throw new RuntimeException("Vote already exists");
        }
        return voteRepo.save(vote);
    }

    @Override
    public void deleteVote(Vote vote) {
        if (!checkVoteExists(vote.getId())) {
            throw new RuntimeException("Vote do not exists");
        }
        voteRepo.deleteByVoteID(vote.getId());
    }

    @Override
    public Vote updateVote(Vote vote) {
        if (checkVoteExists(vote.getId())) {
            throw new RuntimeException("Vote already exists");
        }

        Vote existingVote = voteRepo.findByID(vote.getId());
        existingVote.setId(vote.getId());
        existingVote.setVoteID(vote.getVoteID());
        existingVote.setVote(vote.getVote());
        existingVote.setUserID(vote.getUserID());

        return voteRepo.save(existingVote);
    }

    @Override
    public Boolean checkVoteExists(Long id) {
        return voteRepo.existsById(id);
    }
}