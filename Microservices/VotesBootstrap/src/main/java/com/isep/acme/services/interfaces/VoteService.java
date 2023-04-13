package com.isep.acme.services.interfaces;

import com.isep.acme.model.Vote;

import java.util.List;

public interface VoteService {
    List<Vote> getAllVotes();

    Vote createVote(Vote vote);

    void deleteVote(Vote vote);

    Vote updateVote(Vote vote);

    Boolean checkVoteExists(Long id);
}
