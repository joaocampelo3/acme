package com.isep.acme.services.impl;

import com.isep.acme.events.EventTypeEnum;
import com.isep.acme.events.VoteEvent;
import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.DTO.VoteTempDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.VoteTemp;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.repositories.VoteTempRepository;
import com.isep.acme.services.interfaces.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteTempRepository voteTempRepository;


    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Optional<Vote> findByVoteID(UUID voteID) {
        return repository.findByID(voteID);
    }

    @Override
    public Optional<VoteTemp> findTempByVoteID(UUID voteID) {
        return voteTempRepository.findByID(voteID);
    }

    @Override
    public VoteDTO create(VoteDTO voteDto,UUID reviewUuid) throws Exception {

        final Optional<Review> review = reviewRepository.findByReviewID(reviewUuid);

        if(review.isEmpty()){
            reviewRepository.save(new Review(reviewUuid));
        }

        final Vote v = new Vote(voteDto.getVote(), voteDto.getUserID(), reviewUuid);

        VoteDTO voteDTO = repository.save(v).toDto();

        return voteDTO;
    }

    @Override
    public VoteTempDTO createTemp(VoteTempDTO voteTempDTO, String sku) throws Exception {

        final VoteTemp v = new VoteTemp(UUID.fromString(voteTempDTO.getVoteTempUuid()),voteTempDTO.getVote(), voteTempDTO.getUserID(), voteTempDTO.getReview());

        VoteTempDTO voteTempDTOfinal = voteTempRepository.save(v).toDto();

       return voteTempDTOfinal;
    }

    @Override
    public VoteDTO updateByVoteID(UUID voteID, Vote vote) throws Exception {
        final Optional<Vote> voteToUpdate = repository.findByID(voteID);

        if (voteToUpdate.isEmpty()) return null;

        voteToUpdate.get().updateVote(vote);

        VoteDTO voteDTO = repository.save(voteToUpdate.get()).toDto();

        return voteDTO;
    }

    @Override
    public void deleteByVoteID(UUID voteID) throws Exception {
        final Optional<Vote> v = repository.findByID(voteID);
        repository.deleteByVoteID(voteID);
    }

    @Override
    public void deleteTempByVoteID(UUID voteID) throws Exception {
        final Optional<VoteTemp> v = voteTempRepository.findByID(voteID);
        voteTempRepository.deleteByVoteID(voteID);
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


    @Override
    public Long createReview(Review review) throws Exception {

        review = reviewRepository.save(review);

        return review.getIdReview();
    }

    @Override
    public Boolean DeleteReview(UUID reviewUuid) throws Exception {

        Optional<Review> review = reviewRepository.findByReviewID(reviewUuid);

        if (review.isEmpty()){
            return null;
        }

        reviewRepository.delete(review.get());

        return true;
    }
}
