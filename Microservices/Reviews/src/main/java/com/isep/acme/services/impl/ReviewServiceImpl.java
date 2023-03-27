package com.isep.acme.services.impl;

import com.isep.acme.controllers.ResourceNotFoundException;
import java.lang.IllegalArgumentException;

import com.isep.acme.model.DTO.CreateReviewDTO;
import com.isep.acme.model.DTO.ReviewDTO;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.services.Publisher;
import com.isep.acme.services.RestService;
import com.isep.acme.services.UserService;
import com.isep.acme.services.interfaces.RatingService;
import com.isep.acme.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isep.acme.model.*;

import com.isep.acme.repositories.ReviewRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository repository;

    @Autowired
    UserRepository uRepository;

    @Autowired
    UserService userService;

    @Autowired
    RatingService ratingService;

    @Autowired
    RestService restService;

    @Override
    public Iterable<Review> getAll() {
        return repository.findAll();
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) throws Exception {

        final Optional<Long> productId = repository.findProductIdBySku(sku);

        if(productId.isEmpty()) return null;

        final var user = userService.getUserId(createReviewDTO.getUserID());

        if(user.isEmpty()) return null;

        Rating rating = null;
        Optional<Rating> r = ratingService.findByRate(createReviewDTO.getRating());
        if(r.isPresent()) {
            rating = r.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = restService.getFunFact(date);

        if (funfact == null) return null;

        Review review = new Review(createReviewDTO.getReviewText(), date, productId.get(), funfact, rating, user.get());

        review = repository.save(review);

        if (review == null) return null;

        ReviewDTO reviewDTO = ReviewMapper.toDto(review);

        Publisher.main("Review Create");

        return reviewDTO;
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Long> productId = repository.findProductIdBySku(sku);
        if( productId.isEmpty() ) return null;

        Optional<List<Review>> r = repository.findByProductIdStatus(productId.get(), status);

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public Double getWeightedAverage(Long productId){

        Optional<List<Review>> r = repository.findByProductId(productId);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (Review rev: r.get()) {
            Rating rate = rev.getRating();

            if (rate != null){
                sum += rate.getRate();
            }
        }

        return sum/r.get().size();
    }

    @Override
    public Boolean DeleteReview(Long reviewId) throws Exception {

        Optional<Review> rev = repository.findById(reviewId);

        if (rev.isEmpty()){
            return null;
        }

        Review r = rev.get();
        repository.delete(r);
        Publisher.main("Review Delete");

/*        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {

            return true;
        }*/

        return true;
    }

    @Override
    public List<ReviewDTO> findPendingReview(){

        Optional<List<Review>> r = repository.findPendingReviews();

        if(r.isEmpty()){
            return null;
        }

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = repository.findById(reviewID);

        if(r.isEmpty()){
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if(!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = repository.save(r.get());

        return ReviewMapper.toDto(review);
    }


    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<User> user = uRepository.findById(userID);

        if(user.isEmpty()) return null;

        Optional<List<Review>> r = repository.findByUserId(user.get());

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }
}