package com.isep.acme.services.interfaces;

import com.isep.acme.model.VoteEvent;

import java.util.List;

public interface VoteEventService {
    List<VoteEvent> getAllVotes();

    VoteEvent addVoteEvent(VoteEvent voteEvent);
}
