package com.isep.acme.services.impl;

import com.isep.acme.model.Review;
import com.isep.acme.repository.ReviewRepo;
import com.isep.acme.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepo reviewRepo;

    @Override
    public List<Review> getAllReviews() {
        return reviewRepo.findAll();
    }

    @Override
    public Review createReview(Review review) {
        if (checkReviewExists(review.getIdReview())) {
            throw new RuntimeException("Review already exists");
        }
        return reviewRepo.save(review);
    }

    @Override
    public void deleteReview(Review review) {
        if (!checkReviewExists(review.getIdReview())) {
            throw new RuntimeException("Review do not exists");
        }
        reviewRepo.deleteByReviewID(review.getIdReview());
    }

    @Override
    public Review updateReview(Review review) {
        if (checkReviewExists(review.getIdReview())) {
            throw new RuntimeException("Review already exists");
        }

        Review existingReview = reviewRepo.findByID(review.getIdReview());
        existingReview.setIdReview(review.getIdReview());
        existingReview.setVersion(review.getVersion());
        existingReview.setApprovalStatus(review.getApprovalStatus());
        existingReview.setReviewText(review.getReviewText());
        existingReview.setReport(review.getReport());
        existingReview.setPublishingDate(review.getPublishingDate());
        existingReview.setFunFact(review.getFunFact());
        existingReview.setSku(review.getSku());
        existingReview.setUserId(review.getUserId());
        existingReview.setRating(review.getRating());

        return reviewRepo.save(existingReview);
    }

    @Override
    public Boolean checkReviewExists(Long id) {
        return reviewRepo.existsById(id);
    }
}