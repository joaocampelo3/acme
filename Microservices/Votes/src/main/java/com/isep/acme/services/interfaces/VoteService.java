package com.isep.acme.services.interfaces;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.services.impl.VoteServiceImpl;

import java.util.Optional;

public interface VoteService {

    Optional<VoteReviewDTO> findByVoteID(final Long voteID);

    VoteReviewDTO create(final Vote vote);

    VoteReviewDTO updateByVoteID(final Long voteID, final Vote vote);

    void deleteByVoteID(final Long vote);

}
