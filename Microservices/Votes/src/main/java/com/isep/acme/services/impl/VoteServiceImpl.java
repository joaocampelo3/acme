package com.isep.acme.services.impl;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.services.Publisher;
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
    public Optional<VoteDTO> findByVoteID(Long voteID) {
        return Optional.empty();
    }

    @Override
    public VoteDTO create(Vote vote) throws Exception {
        final Vote v = new Vote(vote.getID(), vote.getVoteID(), vote.getVote(), vote.getUserID());

        VoteDTO voteDTO = repository.save(v).toDto();

        //Publisher.main("Vote Created");

        return voteDTO;
    }

    @Override
    public VoteDTO updateByVoteID(Long voteID, Vote vote) throws Exception {
        final Optional<Vote> voteToUpdate = repository.findByID(voteID);

        if (voteToUpdate.isEmpty()) return null;

        voteToUpdate.get().updateVote(vote);

        VoteDTO voteDTO = repository.save(voteToUpdate.get()).toDto();

        Publisher.main("Vote Updated");

        return voteDTO;
    }

    @Override
    public void deleteByVoteID(Long voteID) throws Exception {
        repository.deleteByVoteID(voteID);
        Publisher.main("Vote Deleted");
    }

    @Override
    public List<VoteDTO> getAll() {
        Iterable<Vote> v = repository.findAll();
        List<VoteDTO> vDto = new ArrayList();
        for (Vote vote : v) {
            vDto.add(vote.toDto());
        }
        return vDto;
    }
}
