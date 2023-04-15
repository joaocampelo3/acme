package com.isep.acme.services.impl;

import com.isep.acme.events.VoteEvent;
import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.DTO.VoteTempDTO;
import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.VoteTemp;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.repositories.VoteTempRepository;
import com.isep.acme.services.MBCommunication.Publisher;
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
    Publisher publisher;

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteTempRepository voteTempRepository;


    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Optional<Vote> findByVoteID(UUID voteID) {
        return Optional.empty();
    }

    @Override
    public VoteDTO create(VoteDTO voteDto,Long reviewID) throws Exception {

        final Optional<Review> review = reviewRepository.findByReviewID(reviewID);

        if(review.isEmpty()) return null;


        final Vote v = new Vote(voteDto.getVote(), voteDto.getUserID(), reviewID);

        VoteDTO voteDTO = repository.save(v).toDto();

        publisher.mainPublish(new VoteEvent(v.getVoteID(), v.getVote()), "vote.vote_created");

        return voteDTO;
    }

    @Override
    public VoteTempDTO createTemp(VoteTempDTO voteTempDTO, String sku) throws Exception {

        final VoteTemp v = new VoteTemp(voteTempDTO.getVote(), voteTempDTO.getUserID(), voteTempDTO.getReview());

        VoteTempDTO voteTempDTOfinal = voteTempRepository.save(v).toDto();

        publisher.mainPublish(new VoteEvent(v.getVoteTempID(), v.getVote(), v.getReview(), sku, v.getUserID()), "vote.voteTemp_created");

        return voteTempDTOfinal;
    }

    @Override
    public VoteDTO updateByVoteID(UUID voteID, Vote vote) throws Exception {
        final Optional<Vote> voteToUpdate = repository.findByID(voteID);

        if (voteToUpdate.isEmpty()) return null;

        voteToUpdate.get().updateVote(vote);

        VoteDTO voteDTO = repository.save(voteToUpdate.get()).toDto();

        publisher.mainPublish(new VoteEvent(voteToUpdate.get().getVoteID(), voteToUpdate.get().getVote()), "vote.vote_updated");

        return voteDTO;
    }

    @Override
    public void deleteByVoteID(UUID voteID) throws Exception {
        final Optional<Vote> v = repository.findByID(voteID);
        repository.deleteByVoteID(voteID);
        publisher.mainPublish(new VoteEvent(v.get().getVoteID()), "vote.vote_deleted");
    }

    @Override
    public void deleteTempByVoteID(UUID voteID) throws Exception {
        final Optional<VoteTemp> v = voteTempRepository.findByID(voteID);
        voteTempRepository.deleteByVoteID(voteID);
        publisher.mainPublish(new VoteEvent(v.get().getVoteTempID()), "vote.voteTemp_deleted");
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
    public Boolean DeleteReview(Long reviewID) throws Exception {

        Optional<Review> review = reviewRepository.findByReviewID(reviewID);

        if (review.isEmpty()){
            return null;
        }

        reviewRepository.delete(review.get());

        return true;
    }
}
