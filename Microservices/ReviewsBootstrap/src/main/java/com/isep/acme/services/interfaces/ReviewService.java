package com.isep.acme.services.interfaces;

import com.isep.acme.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews();

    Review createReview(Review review);

    void deleteReview(Review review);

    Review updateReview(Review review);

    Boolean checkReviewExists(Long id);
}
