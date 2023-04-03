package com.isep.acme.services.interfaces;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.Vote;

import java.util.List;
import java.util.Optional;

public interface VoteService {

    Optional<VoteDTO> findByVoteID(final Long voteID);

    VoteDTO create(final Vote vote) throws Exception;

    VoteDTO updateByVoteID(final Long voteID, final Vote vote) throws Exception;

    void deleteByVoteID(final Long vote) throws Exception;

    List<VoteDTO> getAll();
}
