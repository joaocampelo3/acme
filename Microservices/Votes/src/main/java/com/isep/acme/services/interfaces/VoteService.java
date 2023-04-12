package com.isep.acme.services.interfaces;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.DTO.VoteTempDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VoteService {

    Optional<Vote> findByVoteID(final UUID voteID);

    VoteDTO updateByVoteID(final UUID voteID, final Vote vote) throws Exception;

    void deleteByVoteID(final UUID vote) throws Exception;

    List<VoteDTO> getAll();

    VoteDTO create(VoteDTO voteDTO, Long reviewID) throws Exception;

    VoteTempDTO createTemp(VoteTempDTO voteTempDTO, String sku)throws Exception;

    Long createReview(Review review) throws Exception;

    Boolean DeleteReview(Long reviewID) throws Exception;
}
